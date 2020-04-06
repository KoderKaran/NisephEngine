package LevelEditor;

// Column-Major Form
public class Matrix3 {

    private double[] mat3 = new double[9];

    public Matrix3(){
        mat3[0] = 1; mat3[1] = 0; mat3[2] = 0;
        mat3[3] = 0; mat3[4] = 1; mat3[5] = 0;
        mat3[6] = 0; mat3[7] = 0; mat3[8] = 1;
    }

    public Matrix3(double a0, double a1, double a2, double b0, double b1, double b2, double c0, double c1, double c2){
        mat3[0] = a0; mat3[1] = a1; mat3[2] = a2;
        mat3[3] = b0; mat3[4] = b1; mat3[5] = b2;
        mat3[6] = c0; mat3[7] = c1; mat3[8] = c2;
    }

    public Matrix3 add(Matrix3 addMat){
        return new Matrix3(
                mat3[0] + addMat.get(0,0), mat3[1] + addMat.get(0,1), mat3[2] + addMat.get(0,2),
                mat3[3] + addMat.get(1,0), mat3[4] + addMat.get(1,1), mat3[5] + addMat.get(1,2),
                mat3[6] + addMat.get(2,0), mat3[7] + addMat.get(2,1), mat3[8] + addMat.get(2,2)
        );
    }

    public Matrix3 add(double scalar){
        return new Matrix3(
                mat3[0] + scalar, mat3[1] + scalar, mat3[2] + scalar,
                mat3[3] + scalar, mat3[4] + scalar, mat3[5] + scalar,
                mat3[6] + scalar, mat3[7] + scalar, mat3[8] + scalar
        );
    }

    public Matrix3 sub(Matrix3 subMat){
        return new Matrix3(
                mat3[0] - subMat.get(0,0), mat3[1] - subMat.get(0,1), mat3[2] - subMat.get(0,2),
                mat3[3] - subMat.get(1,0), mat3[4] - subMat.get(1,1), mat3[5] - subMat.get(1,2),
                mat3[6] - subMat.get(2,0), mat3[7] - subMat.get(2,1), mat3[8] - subMat.get(2,2)
        );
    }

    public Matrix3 mul(Matrix3 mulMat){
        double b0 = mulMat.get(0,0);
        double b1 = mulMat.get(0,1);
        double b2 = mulMat.get(0,2);
        double b3 = mulMat.get(1,0);
        double b4 = mulMat.get(1,1);
        double b5 = mulMat.get(1,2);
        double b6 = mulMat.get(2,0);
        double b7 = mulMat.get(2,1);
        double b8 = mulMat.get(2,2);
        return new Matrix3(
                mat3[0]*b0 + mat3[1]*b3 + mat3[2]*b6, mat3[0]*b1 + mat3[1]*b4 + mat3[2]*b7, mat3[0]*b2 + mat3[1]*b5 + mat3[2]*b8,
                mat3[3]*b0 + mat3[4]*b3 + mat3[5]*b6, mat3[3]*b1 + mat3[4]*b4 + mat3[5]*b7, mat3[3]*b2 + mat3[4]*b5 + mat3[5]*b8,
                mat3[6]*b0 + mat3[7]*b3 + mat3[8]*b6, mat3[6]*b1 + mat3[7]*b4 + mat3[8]*b7, mat3[6]*b2 + mat3[7]*b5 + mat3[8]*b8
        );
    }

    public Vector3d mul(Vector3d vec){
        return new Vector3d(
                mat3[0]*vec.getX() + mat3[1]*vec.getY() + mat3[2]*vec.getZ(),
                mat3[3]*vec.getX() + mat3[4]*vec.getY() + mat3[5]*vec.getZ(),
                mat3[6]*vec.getX() + mat3[7]*vec.getY() + mat3[8]*vec.getZ()
        );
    }

    public Matrix3 inverse(){
        //TODO: Implement.
        return null;
    }

    public static Matrix3 identity(){
        return new Matrix3();
    }

    public double determinant() {
        /* double a1 = mat3[0];
        double a2 = mat3[1];
        double a3 = mat3[2];
        double b1 = mat3[3];
        double b2 = mat3[4];
        double b3 = mat3[5];
        double c1 = mat3[6];
        double c2 = mat3[7];
        double c3 = mat3[8];
        a1*b2*c3 - a1*b3*c2 + a2*b3*c1 - a2*b1*c3 + a3*b1*c2 - a3*b2*c1 */
        return mat3[0]*mat3[4]*mat3[8] - mat3[0]*mat3[5]*mat3[7] + mat3[1]*mat3[5]*mat3[6] - mat3[1]*mat3[3]*mat3[8] + mat3[2]*mat3[3]*mat3[7] - mat3[2]*mat3[4]*mat3[6];
    }
    // Column-Major Form
    public static Matrix3 translate(Vector3d translate){
        return new Matrix3(
                1, 0, translate.getX(),
                0, 1, translate.getY(),
                0, 0, 1
        );
    }
    // Column-Major Form
    public static Matrix3 rotate(double degreesClockwise){
        double angleInRadians = (degreesClockwise * Math.PI / 180) * -1; // mult by -1 to reverse directions, make it clock wise.
        double cosOfAngle = Math.cos(angleInRadians);
        double sinOfAngle = Math.sin(angleInRadians);
        return new Matrix3(
                cosOfAngle, -sinOfAngle, 0,
                sinOfAngle, cosOfAngle, 0,
                0, 0, 1
        );
    }
    // Column-Major Form
    public static Matrix3 scale(Vector3d scale){
        return new Matrix3(
                scale.getX(),0,0,
                0,scale.getY(),0,
                0,0,1
        );
    }
    // Column-Major Form
    public static Matrix3 shear(Vector3d shear) {
        return new Matrix3(
                1,shear.getX(),0,
                shear.getY(),1,0,
                0,0,1
        );
    }

    public double get(int row, int column){
        return mat3[row * 3 + column];
    }
}
