public class Earth extends Model {

    //Vector x0; // Эфемериды [X, Y, Z, Vx, Vy, Vz] в инерциальной
                    //гелиоцентрической экваториальной системе координат
    Vector re; // Радиус вектор Земли в гелиоцентрической/геоцентрической СК
    double R = 6371.3e3;
    double omega = 7.292115e-5; // Угловая скорость вращения Земли
    double nu = 132712.43994e6; // гелиоцентрическая гравитационная постоянная
    double s; // Текущее звездное время

    Gnomon gnom; // Гномон на Земле

    public Earth(double t0, double t1, double h, Vector x0)
    {
        super(t0, t1, h);
        result = new Matrix(new double[][]{
                {x0.data[0]},
                {x0.data[1]},
                {x0.data[2]},
                {x0.data[3]},
                {x0.data[4]},
                {x0.data[5]},
        });
        this.x0 = x0;
    }

    @Override
    public Vector getRight(Vector vec, double t)
    {
        double norm = Math.sqrt(Math.pow(vec.data[0], 2) + Math.pow(vec.data[1], 2)
                + Math.pow(vec.data[2], 2));
        Vector V = new Vector(new double[]{vec.data[3], vec.data[4], vec.data[5]});
        Vector X = new Vector(new double[]{vec.data[0], vec.data[1], vec.data[2]});
        Vector res = new Vector(new double[]{
                vec.data[3],
                vec.data[4],
                vec.data[5],
                -nu*vec.data[0]/Math.pow(norm,3),
                -nu*vec.data[1]/Math.pow(norm,3),
                -nu*vec.data[2]/Math.pow(norm,3)
        });
        return res;
    }

    public void rotation(double sg0, double t, double lambda)
    {
        s = sg0 + omega*t + lambda;
    }


}
