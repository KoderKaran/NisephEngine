package LevelEditor;

public class Vector3d {

    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d add(Vector3d vecToAdd){
        return new Vector3d(x + vecToAdd.getX(), y + vecToAdd.getY(), z + vecToAdd.getZ());
    }

    public Vector3d sub(Vector3d vecToSub){
        return new Vector3d(x - vecToSub.getX(), y - vecToSub.getY(), z - vecToSub.getZ());
    }

    public Vector3d scalarMul(double scalar) {
        return new Vector3d(x * scalar, y * scalar, z * scalar);
    }

    public double dotProduct(Vector3d vecToMul) {
        return (x * vecToMul.getX()) + (y * vecToMul.getX()) + (z * vecToMul.getZ());
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3d normalize(){
        double len = length();
        return new Vector3d(x/len, y/len, z/len);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getZ(){
        return z;
    }
}
