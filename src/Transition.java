public class Transition {
    public static double fromGradToRad(double fi)
    {
        return fi*Math.PI/180;
    }
    public static Vector fromISKtoTSK(double fi, double s, Vector vec, double t)
    {
        double om = vec.data[1];
        double la = vec.data[2];
        Matrix A = new Matrix(new double[][]
                {
                        {-Math.sin(fi)*Math.cos(s), Math.cos(fi)*Math.cos(s), -Math.sin(s)},
                        {-Math.sin(fi)*Math.sin(s), Math.cos(fi)*Math.sin(s), Math.cos(s)},
                        {Math.cos(fi), Math.sin(fi), 0}
                });
        return A.flip().mult(vec);
    }

    public static Vector fromTSKtoISK(Gnomon gnomon, Vector vec, double t)
    {
        double fi = vec.data[0];
        double s = gnomon.s;
        Matrix A = new Matrix(new double[][]
                {
                        {-Math.sin(fi)*Math.cos(s), Math.cos(fi)*Math.cos(s), -Math.sin(s)},
                        {-Math.sin(fi)*Math.sin(s), Math.cos(fi)*Math.cos(s), Math.cos(s)},
                        {Math.cos(fi), Math.sin(fi), 0}
                });
        return A.flip().inverse().mult(vec);
    }

    public static Vector fromGSKtoISK(double fi, double s, double Re, double t)
    {
        return new Vector(new double[]{
                Re*Math.cos(fi)*Math.cos(s),
                Re*Math.cos(fi)*Math.sin(s),
                Re*Math.sin(fi)
        });
    }

}
