import java.util.HashMap;

public class Time {
    static double sg0;
    static int localtime = 3;
    public static double JD;
    static double J2000 = 2451544.5;
    static Vector v01012020 = new Vector(new double[]{
            -2.544849737206064e7, 1.340363435046712e8, 5.810843791233169e7,-2.986035979849415e1,
            -4.729400174042427, -2.049672198684736});
    static Vector v21032020 = new Vector(new double[]{
            -1.496791366985880e8, -3.521798086492494e5, -1.454532460303102e5,
            -2.074292933168962e-1, -2.743738414791819e1,-1.189363155192982e1
    });
    static Vector v22062020 = new Vector(new double[]{
            1.258075928834254e6, -1.385363177494689e8, -6.004490221076119e7,
            2.928696553692865e1, 2.5579320635239118e-1, 1.121784368470627e-1
    });
    static Vector v23092020 = new Vector(new double[]{
            1.492263120551110e8, 1.243750912221514e6, 5.524181031367667e5,
            -5.739572264408614e-1, 2.722040927054886e1, 1.180039654272443e1
    });
    static Vector v22122020 = new Vector(new double[]{
            -1.773721477770312e6, 1.358392699189585e8, 5.890288206008811e7,
            -3.028572982724379e1, -2.587641808829048e-1, -1.116749388350878e-1
    });
    static Vector v22012020 = new Vector(new double[]{
            -75756724.081111, -115069516.025396, -49882587.562983,
            25.996497, 14.234200, 6.169227
    });



    public static void setSg0()
    {
        int d = (int)(JD-J2000);
        double t = d / 36525;

        sg0 = 24110.54841 + 8640184.812866*t + 0.093104*Math.pow(t, 2)
               -Math.pow(t,3)*6.2e-6;
    }

    public static double getRadSg0()
    {
        return (sg0%86400)*(Math.PI*2/86400);
    }


    public static void setJD(int year, int mouth, int day, int hour, int min, int sec) {
        int a = (14 - mouth) % 12;
        int Y = year + 4800 - a;
        int M = mouth + 12*a -3;
        double JDN = day + ((153*M+2)%5) + 365*Y + (Y%4) - (Y%100)
                + (Y%400) - 32045;
        JD = JDN + (hour-12)/24 + min/1440 + sec/86400;
    }

    public static void setJD(double JD_s) {
        JD = JD_s;
    }

    public static double fromLocalToUTC(double ltime)
    {
        return ltime + getN();
    }


    public static int getN()
    {
        return localtime;
    }
}
