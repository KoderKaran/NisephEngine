package LevelEditor;

public class Vector2d {

	private double x;
	private double y;

	public Vector2d(double x, double y){
		this.x = x;
		this.y = y;
	}

	//Adding two vectors (A and B) takes you from the start of A to the end of B. (Hypotenuse of a triangle). Can work with many vectors.
	public Vector2d add(Vector2d vecToAdd){
		return new Vector2d(x + vecToAdd.getX(), y + vecToAdd.getY());
	}

	//If going from point A to point B, subtract the coordinates of A from the coordinates of B. (B-A)
	public Vector2d sub(Vector2d vecToSub){
		return new Vector2d(x - vecToSub.getX(), y - vecToSub.getY());
	}

	//Pass -1 to change direction
	public Vector2d scalarMul(double scalar) {
		return new Vector2d(x * scalar, y * scalar);
	}

	public double dotProduct(Vector2d vecToMul) {
		return (x * vecToMul.getX()) + (y * vecToMul.getX());
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}
}
