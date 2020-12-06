package com.kicorangel.repr.ldse;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class FreqMngr {
    
    public static void Test() {
        ArrayList<Double> a = GetTestArray();
        a = SortValues(a);
        System.out.println("N: " + N(a));
        System.out.println("Sum: " + Sum(a));
        System.out.println("Prod: " + Prod(a));
        System.out.println("Avg: " + Avg(a));
        System.out.println("G: " + G(a));
        System.out.println("Std: " + Std(a));
        System.out.println("Median: " + Median(a));
        System.out.println("Min: " + Min(a));
        System.out.println("Max: " + Max(a));
        System.out.println("Q1: " + Q1(a));
        System.out.println("Q3: " + Q3(a));
        
        for (int i=1;i<=10;i++) {
            System.out.println("M" + i + ": " + M(a, i));
        }

        System.out.println("g1: " + (M(a, 3)/Math.pow(Std(a), 3)));
        System.out.println("g2: " + ((M(a, 4)/Math.pow(Std(a), 4))-3));
        System.out.println();
    }
    
    public static ArrayList<Double> SortValues(ArrayList<Double> Values) {
        Collections.sort(Values);
        return Values;
    }
    
    public static double N(ArrayList<Double> Values) {
        return Values.size();
    }
    
    public static double Sum(ArrayList<Double> Values) {
        double Sum = 0;
        for (int i=0;i<Values.size();i++) {
            Sum += Values.get(i);
        }
        return Sum;
    }
    
    public static double Prod(ArrayList<Double> Values) {
        double P = 1;
        for (int i=0;i<Values.size();i++) {
            P *= Values.get(i);
        }
        return P;
    }
    
    public static double Avg(ArrayList<Double> Values) {
        if (N(Values)==0) {
            return 0;
        } else {
            return Sum(Values)/N(Values);
        }
    }
    
    public static double Std(ArrayList<Double> Values) {
        double N = N(Values);
        
        if (N==0) {
            return 0;
        } else {
            double Std = 0;
            double Avg = Avg(Values);


            for (int i=0;i<Values.size();i++) {
                Std += Math.pow(Values.get(i) - Avg, 2);
            }
            Std = Math.sqrt(Std/N);
            return Std;
        }
    }
    
    public static double G(ArrayList<Double> Values) {
        return Math.pow(Prod(Values), 1/N(Values));
    }
    
    public static double Median(ArrayList<Double> Values) {
        double N = N(Values);
        
        if (N==0) {
            return 0;
        } else if (N==1) {
            return Values.get(0);
        } else if (N==2) {
            return (Values.get(0)+Values.get(1))/2;
        } else {
            double median = 0;

            int m = (int)Math.round(N/2) - 1;

            if (N%2==0) {
                median = (Values.get(m) + Values.get(m+1)) / 2;
            } else {
                median = Values.get(m);
            }

            return median;
        }
    }
    
    public static double Q1(ArrayList<Double> Values) {
        double N = N(Values);
        if (N==0) {
            return 0;
        } else if (N==1) {
            return Values.get(0);
        } else if (N==2 || N==3) {
            return (Values.get(0)+Values.get(1))/2;
        } else {
            double p = (N/4);
            int pos = (int)Math.round(p);
            int d = (pos>p)?-1:1;
            
            return (Values.get(pos-1) + Values.get(pos+d-1)) / 2;
        }
    }
    
    public static double Q3(ArrayList<Double> Values) {
        double N = N(Values);
        if (N==0) {
            return 0;
        } else if (N==1) {
            return Values.get(0);
        } else if (N==2 || N==3) {
            return (Values.get(0)+Values.get(1))/2;
        } else {
            double p = (3*N/4); 
            int pos = (int)Math.round(p);
            int d = (pos>p)?-1:1;

            return (Values.get(pos-1) + Values.get(pos+d-1)) / 2;
        }
    }
    
    public static double Min(ArrayList<Double> Values) {
        if (N(Values)==0) {
            return 0;
        } else {
            return Values.get(0);
        }
    }
    
    public static double Max(ArrayList<Double> Values) {
        double N = N(Values);
        if (N==0) {
            return 0;
        } else {
            return Values.get((int)N(Values)-1);
        }
    }
    
    public static double M(ArrayList<Double> Values, int h) {
        double m = 0;
        double mean = Avg(Values);
        double N = N(Values);
        for (int i=0;i<(int)N;i++) {
            double xi = Values.get(i);
            m += (Math.pow(xi-mean, h) / N);
        }
        
        return m;
    }
    
    public static ArrayList<Double> GetTestArray() {
        ArrayList<Double> oTest = new ArrayList<Double>();
        oTest.add(0.05128205128205128);oTest.add(0.006578947368421052);oTest.add(0.08);oTest.add(0.05405405405405406);oTest.add(0.13147410358565736);oTest.add(0.23376623376623376);oTest.add(0.05138339920948617);oTest.add(0.0751730959446093);oTest.add(0.026501766784452298);oTest.add(0.03787878787878788);oTest.add(0.019230769230769232);oTest.add(0.05765407554671968);oTest.add(0.06630837388336118);oTest.add(0.06156156156156156);oTest.add(0.02561957544703545);oTest.add(0.04441776710684274);oTest.add(0.08487654320987655);oTest.add(0.06322660021478693);oTest.add(0.05977432143946325);oTest.add(0.06116207951070336);oTest.add(0.0297029702970297);oTest.add(0.061848579440241254);oTest.add(0.02997275204359673);oTest.add(0.052980132450331126);oTest.add(0.1111111111111111);oTest.add(0.05706760316066725);oTest.add(0.051347881899871634);oTest.add(0.05311111111111111);oTest.add(0.05501813784764208);oTest.add(0.07098197866862817);oTest.add(0.04316320100819156);oTest.add(0.15151515151515152);oTest.add(0.030303030303030304);oTest.add(0.060932642487046634);oTest.add(0.04296058706322593);oTest.add(0.02938706968933669);oTest.add(0.06666666666666667);oTest.add(0.046010483401281305);oTest.add(0.01510989010989011);oTest.add(0.06459475929311395);oTest.add(0.052132701421800945);oTest.add(0.04429530201342282);oTest.add(0.023622047244094488);oTest.add(0.08035714285714286);oTest.add(0.0423841059602649);oTest.add(0.20833333333333334);oTest.add(0.046153846153846156);oTest.add(0.030303030303030304);oTest.add(0.05378973105134474);oTest.add(0.03759398496240601);oTest.add(0.03389830508474576);oTest.add(0.06772207563764292);oTest.add(0.03614457831325301);oTest.add(0.04081632653061224);oTest.add(0.02312925170068027);oTest.add(0.10046728971962617);oTest.add(0.07534246575342465);oTest.add(0.04990980156343957);oTest.add(0.0038314176245210726);oTest.add(0.125);oTest.add(0.036093418259023353);oTest.add(0.04782146652497343);oTest.add(0.022675736961451247);oTest.add(0.015810276679841896);oTest.add(0.024764150943396228);oTest.add(0.09090909090909091);oTest.add(0.20065075921908893);oTest.add(0.11842105263157894);oTest.add(0.06797880393641181);oTest.add(0.05958971019211983);oTest.add(0.06593406593406594);oTest.add(0.06195414847161572);oTest.add(0.05688622754491018);oTest.add(0.029411764705882353);oTest.add(0.06042720629567173);oTest.add(0.01495581237253569);oTest.add(0.059329710144927536);oTest.add(0.014423076923076924);oTest.add(0.006696428571428571);oTest.add(0.007874015748031496);oTest.add(0.036931818181818184);oTest.add(0.033950617283950615);oTest.add(0.05710955710955711);oTest.add(0.04783764194249819);oTest.add(0.05770594492376788);oTest.add(0.047713559860273105);oTest.add(0.04827586206896552);oTest.add(0.03899721448467967);oTest.add(0.04046242774566474);oTest.add(0.08487084870848709);oTest.add(0.09398496240601503);oTest.add(0.060836501901140684);oTest.add(0.037368625924484235);oTest.add(0.062254025044722716);oTest.add(0.03506787330316742);oTest.add(0.07714422616195496);oTest.add(0.00528169014084507);oTest.add(0.04974134500596896);oTest.add(0.05132895326265492);oTest.add(0.02197802197802198);oTest.add(0.10226385636221702);oTest.add(0.09375);oTest.add(0.060362173038229376);oTest.add(0.029714285714285714);oTest.add(0.06179174684626897);oTest.add(0.05933062880324544);oTest.add(0.06086956521739131);oTest.add(0.04696969696969697);oTest.add(0.03125);oTest.add(0.04486626402070751);oTest.add(0.06339417858904785);oTest.add(0.12087912087912088);oTest.add(0.03877423389618512);oTest.add(0.045454545454545456);oTest.add(0.05233160621761658);oTest.add(0.08433079434167573);oTest.add(0.06430568499534017);oTest.add(0.036585365853658534);oTest.add(0.03803131991051454);oTest.add(0.012195121951219513);oTest.add(0.03070175438596491);oTest.add(0.012684989429175475);oTest.add(0.056);oTest.add(0.031069789337413205);oTest.add(0.11019490254872563);oTest.add(0.03907849829351536);oTest.add(0.05070656691604322);oTest.add(0.040577545431914365);oTest.add(0.04275653923541248);oTest.add(0.002749770852428964);oTest.add(0.059729418537708696);oTest.add(0.059192200557103065);oTest.add(0.09090909090909091);oTest.add(0.061866125760649086);oTest.add(0.03822441430332922);oTest.add(0.07957425240750127);oTest.add(0.056818181818181816);oTest.add(0.05223270440251572);oTest.add(0.034278959810874705);oTest.add(0.02541371158392435);oTest.add(0.07783072384842296);oTest.add(0.02029520295202952);oTest.add(0.09235668789808917);oTest.add(0.14285714285714285);oTest.add(0.04772141014617369);oTest.add(0.051194539249146756);oTest.add(0.05726518597805726);oTest.add(0.02666666666666667);oTest.add(0.024875621890547265);oTest.add(0.056580565805658053);oTest.add(0.09993084370677732);oTest.add(0.03160667251975417);oTest.add(0.014204545454545454);oTest.add(0.057746478873239436);oTest.add(0.04020100502512563);oTest.add(0.030927835051546393);oTest.add(0.03538844346143213);oTest.add(0.1069182389937107);oTest.add(0.07647740440324449);oTest.add(0.060077519379844964);oTest.add(0.06882591093117409);oTest.add(0.09913631242959069);oTest.add(0.044444444444444446);oTest.add(0.007462686567164179);oTest.add(0.15384615384615385);oTest.add(0.045454545454545456);oTest.add(0.020202020202020204);oTest.add(0.04666666666666667);oTest.add(0.02237136465324385);oTest.add(0.03278688524590164);oTest.add(0.054481546572934976);oTest.add(0.04155495978552279);oTest.add(0.06364301389904901);oTest.add(0.015267175572519083);oTest.add(0.0417036379769299);oTest.add(0.05908850026497085);oTest.add(0.059233449477351915);oTest.add(0.07114624505928854);oTest.add(0.08196721311475409);oTest.add(0.04513888888888889);oTest.add(0.013858497447118891);oTest.add(0.03409090909090909);oTest.add(0.044311887515977845);oTest.add(0.06677821721934896);oTest.add(0.053776540201879566);oTest.add(0.08584534731323722);oTest.add(0.13934426229508196);oTest.add(0.0392156862745098);oTest.add(0.04);oTest.add(0.24087591240875914);oTest.add(0.03496503496503497);oTest.add(0.1726844583987441);oTest.add(0.024930747922437674);oTest.add(0.07839489768801489);oTest.add(0.03914590747330961);oTest.add(0.07727272727272727);oTest.add(0.13043478260869565);oTest.add(0.14685314685314685);oTest.add(0.06227246598965319);oTest.add(0.08);oTest.add(0.04920324294101202);oTest.add(0.040156709108716944);oTest.add(0.05605381165919283);oTest.add(0.05672777585610515);oTest.add(0.05665501785307301);oTest.add(0.0507009239105618);oTest.add(0.05225240864508289);oTest.add(0.05952699091394976);oTest.add(0.045612510860121636);oTest.add(0.045299630786708324);oTest.add(0.04524230735767524);oTest.add(0.04798105682951147);oTest.add(0.04971127290986693);oTest.add(0.07126258714175059);oTest.add(0.08287292817679558);oTest.add(0.05076199013895114);oTest.add(0.06537530266343826);oTest.add(0.08333333333333333);oTest.add(0.021875);oTest.add(0.10372492836676218);oTest.add(0.08149405772495756);oTest.add(0.05713271823988644);oTest.add(0.06647398843930635);oTest.add(0.07170542635658915);oTest.add(0.020801623541349568);oTest.add(0.041198028560596484);oTest.add(0.05487411233053583);oTest.add(0.05263157894736842);oTest.add(0.050505050505050504);oTest.add(0.03242147922998987);oTest.add(0.03339123721289326);oTest.add(0.064752596212584);oTest.add(0.06244175209692451);oTest.add(0.04959568733153639);oTest.add(0.047077492726791854);oTest.add(0.04957791772745545);oTest.add(0.0626658501735048);oTest.add(0.009955909543450435);oTest.add(0.053642914331465175);oTest.add(0.07070217917675545);oTest.add(0.04739336492890995);oTest.add(0.02620689655172414);oTest.add(0.0440291415901172);
        return oTest;
    }
    
   
}
