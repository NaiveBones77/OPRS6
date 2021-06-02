import java.util.ArrayList;

public class Gnomon {
    ArrayList<Vector> listsh = new ArrayList<>(); //список векторов с координатами в ТСК за день
    ArrayList<Double> listgrad = new ArrayList<>();
    ArrayList<Double> listtime = new ArrayList<>();
    ArrayList<Integer> dataday = new ArrayList<>();

    static double tau = 0;

    double sg0;
    double la;
    double s; //текущее звездное время гринвическое
    double l0; // Длина гномона
    Vector xtop = new Vector(new double[3]);  // Топоцентрическая ск
    Vector r0 = new Vector(new double[3]);
    Vector rg = new Vector(new double[3]);
    Vector re0 = new Vector(new double[3]);
    Vector re = new Vector(new double[3]);
    Vector rezv = new Vector(new double[3]);
    Vector rsh = new Vector(new double[3]); // Вектор тени
    Vector x = new Vector(new double[3]); // ИСК
    public Earth earth;
    double daylight=0;

    public Gnomon(double l0, Earth ea, double lambda, double fi)
    {
        earth = ea;
        xtop.data[0] = Transition.fromGradToRad(lambda);
        xtop.data[1] = Transition.fromGradToRad(fi);
        xtop.data[2] = earth.R;
        this.l0 = l0;
    }

    public double getRadiansForS(double s)
    {
        return (s%86400)*(Math.PI*2/86400);
    }



    public void rotation()
    {
        Time.setSg0();
        int i = 1; // начинаем с 2 значения в матрице result
        for (int t = Time.localtime*60*60; t <= (24 + Time.localtime)* 60 * 60 ; t+=60) {
            sg0 = getRadiansForS(Time.sg0);
//            la = Transition.fromGradToRad(xtop.data[0]);
            s = sg0 + earth.omega*t + xtop.data[0];
            x = Transition.fromGSKtoISK(xtop.data[1], s, this.earth.R,  t);
            r0 = x.mult(1./x.getMagnitude());
            rg = r0.mult(l0);
            re.data[0] = earth.result.data[0][i];
            re.data[1] = earth.result.data[1][i];
            re.data[2] = earth.result.data[2][i];
            re0 = re.mult(1./re.getMagnitude());
            rezv = re0.mult(-l0/(re0.mult(r0)));
            rsh = rg.sum(rezv);
            rsh = Transition.fromISKtoTSK(xtop.data[1], s, rsh, t);
            double cosalfa = re.mult(x)/(re.getMagnitude()*x.getMagnitude());
//            alfa = -Math.asin(re0.mult(r0)/(re0.getMagnitude()*r0.getMagnitude()));
            listgrad.add(cosalfa);
            if (cosalfa < 0 )
            {
                if (Math.sqrt(Math.pow(rsh.data[0], 2) + Math.pow(rsh.data[2], 2) ) < 10)
                    listsh.add(rsh);
                if ( (t >= 28800)  && (t <= 72000) )
                    daylight += 1;

            }
            i++; //Костыль
        }
        daylight/=60;
    }

    public void rotation2()
    {
        Time.setSg0();
        int i = 1; // начинаем с 2 значения в матрице result
        for (int t = Time.localtime*60*60; t < (24 + Time.localtime)* 60 * 60 ; t+=60) {
            sg0 = getRadiansForS(Time.sg0);
//            la = Transition.fromGradToRad(xtop.data[0]);
            s = sg0 + earth.omega*tau + xtop.data[0];
            x = Transition.fromGSKtoISK(xtop.data[1], s, this.earth.R,  tau);
            r0 = x.mult(1./x.getMagnitude());
            rg = r0.mult(l0);
            re.data[0] = earth.result.data[0][i];
            re.data[1] = earth.result.data[1][i];
            re.data[2] = earth.result.data[2][i];
            re0 = re.mult(1./re.getMagnitude());
            rezv = re0.mult(-l0/(re0.mult(r0)));
            rsh = rg.sum(rezv);
            rsh = Transition.fromISKtoTSK(xtop.data[1], s, rsh, tau);
            double cosalfa = re.mult(x)/(re.getMagnitude()*x.getMagnitude());
//            alfa = -Math.asin(re0.mult(r0)/(re0.getMagnitude()*r0.getMagnitude()));
            listgrad.add(cosalfa);
            if (cosalfa < 0 )
            {
                listsh.add(rsh);
//                if ( (t >= 28800)  && (t <= 72000) )
                    daylight += 1;

            }
            i++; //Костыль
            tau+=60;
        }
        daylight/=60;
    }

    public void getDaylight(int utc)
    {
        Time.setSg0();
        boolean isday=false;
        double timeday = 0;
        int day = 0;
        int hour = 0  + utc - 1;
        double s = sg0 + xtop.data[1];
        int i = 0;
        while (day < 365)
        {
            Vector v = new Vector(new double[]{earth.result.data[0][i], earth.result.data[1][i],
                    earth.result.data[2][i]});
            sg0 = getRadiansForS(Time.sg0);
//            la = Transition.fromGradToRad(xtop.data[0]);
            s = sg0 + earth.omega*(i*60) + xtop.data[0];
            x = Transition.fromGSKtoISK(xtop.data[1], s, this.earth.R,  i*60);
            //r0 = x.mult(1/x.getMagnitude());
            rg = r0.mult(l0);
            re.data[0] = earth.result.data[0][i];
            re.data[1] = earth.result.data[1][i];
            re.data[2] = earth.result.data[2][i];
//            re0 = re.mult(1/re.getMagnitude());
//            rezv = re0.mult(-l0/(re0.mult(r0)));
//            rsh = rg.sum(rezv);
//            rsh = Transition.fromISKtoTSK(xtop.data[1], s, rsh, t);
            double alfa = Math.acos(re.mult(x)/(re.getMagnitude()*x.getMagnitude()));
            if (alfa >= Math.PI/2)
                isday = true;
            if (alfa >= Math.PI/2 && (hour >=8 && hour <=20))
                timeday+=1;
            if (alfa < Math.PI/2 && isday)
            {
                isday = false;
                listtime.add(timeday / 60);
                dataday.add(day);
                timeday = 0;

            }
            if (i % 60 == 1)
                hour+=1;
            if (hour == 24)
            {
                hour = 0;
                day+=1;
            }
            i++;
        }
    }


}
