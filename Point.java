public class Point{
    public double x,y;
    public String myString;

    public Point(){
	this(0, 0);
	myString = " ";
    }

    public Point (double xIn, double yIn){
	x = xIn;
	y = yIn;
	checkAxis();
    }

    public String toString() {
	return myString;
    }

    public void checkAxis() {
	if(x==0 && y==0)
	    myString="+";
	else if(x==0)
	    myString="|";
	else if(y==0)
	    myString="-";
	else
	    myString=" ";
    }

    public void checkAxis(double halfInc) {
	boolean nearX = x < halfInc && x > -1*halfInc;
	boolean nearY = y < halfInc && y > -1*halfInc;

	if (nearX && nearY)
	    myString = "+";
	else if (nearX)
	    myString = "|";
	else if (nearY)
	    myString = "-";
	else
	    myString = " ";
    }
    
    public boolean subEq(String eq){
	eq = MathString.sub(eq,"x",x);
	eq = MathString.sub(eq,"y",y);
	return (MathString.isEqual(eq));
    }

    public void closeEnough(String eq, double halfInc){
	String center = MathString.sub(eq, "x", x);
	center = MathString.sub(center, "y", y);
	center = MathString.evaluateParens(center);
	int divZeroIndex = center.indexOf("/0.0");
	boolean numNext;
	if (center.length() > divZeroIndex + 4) {
	    String nextChar = center.substring(divZeroIndex+4, divZeroIndex+5);
	    numNext = MathString.getNumbers().indexOf(nextChar) != -1;
	} else
	    numNext = false;
	
	if (divZeroIndex != -1 && halfInc > 0.001 && !numNext) {
	    //handle asymptotes
	    checkAxis(halfInc);
	    
	    double origX = x;
	    double origY = y;
	    
	    setCor(origX + halfInc/2, origY + halfInc/2);
	    closeEnough(eq, halfInc/2 - 0.01);
	    if (!myString.equals("*")) {
		setCor(origX + halfInc/2, origY - halfInc/2);
		closeEnough(eq, halfInc/2 - 0.01);
	    }
	    if (!myString.equals("*")) {
		setCor(origX - halfInc/2, origY - halfInc/2);
		closeEnough(eq, halfInc/2 - 0.01);
	    }
	    if (!myString.equals("*")) {
		setCor(origX - halfInc/2, origY + halfInc/2);
		closeEnough(eq, halfInc/2 - 0.01);
	    }
	    setCor(origX, origY);

	    if (!myString.equals("*"))
		checkAxis(halfInc);
	    
	    return;
	}
	
	boolean positives = false;
	boolean negatives = false;

	String eq1 = MathString.sub(eq,"x",x + halfInc);
	//System.out.println(eq1);
	eq1= MathString.sub(eq1,"y",y + halfInc);
	//System.out.print(MathString.subSides(eq1));

	if(MathString.subSides(eq1) == -1)
	    negatives = true;
	else if(MathString.subSides(eq1) == 1)
	    positives = true;

	
	String eq2 = MathString.sub(eq,"x",x - halfInc);
	eq2 = MathString.sub(eq2,"y",y - halfInc);
	//System.out.print(MathString.subSides(eq2));
	if(MathString.subSides(eq2) == -1)
	    negatives = true;
	else if(MathString.subSides(eq2) == 1)
	    positives = true;
	   
	String eq3 = MathString.sub(eq,"x",x + halfInc);
	eq3 = MathString.sub(eq3,"y",y - halfInc);
	//System.out.print(MathString.subSides(eq3));
	if(MathString.subSides(eq3) == -1)
	    negatives = true;
	else if(MathString.subSides(eq3) == 1)
	    positives = true;	

	String eq4 = MathString.sub(eq,"x",x - halfInc);
	eq4 = MathString.sub(eq4,"y",y + halfInc);
	//System.out.print(MathString.subSides(eq4) + "\n");
	if(MathString.subSides(eq4) == -1)
	    negatives = true;
	else if(MathString.subSides(eq4) == 1)
	    positives = true;

	if (positives && negatives) 
	    myString = "*";
	else {
	    myString = " ";
	    checkAxis(halfInc);
	}
    }

    public void setCor(double X, double Y){
	x = X;
	y = Y;
    }

    public double[] getCor() {
	double[] coords = {x, y};
	return coords;
    }
    
    public void translate(double dx, double dy) {
	x += dx;
	y += dy;
	checkAxis();
    }

    public static void main(String[] args) {
	Point p = new Point(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
	
	String eq = "y=1/x";
	p.closeEnough(eq, Double.parseDouble(args[2]));
	System.out.println(p);
    }

}
