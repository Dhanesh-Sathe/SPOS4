import java.io.*;
import java.util.*;

class pass2_m {

    String mnt[][], mdt[][], ala[][];
    int a = 0;
    int mntc, mdtc, alac;
    boolean b;
    int c_mdt, c_ala;

    pass2_m() {
        mnt = new String[5][4];
        mdt = new String[15][3];
        for(int j=0;j<15;j++)
            {for(int i=0;i<3;i++)
            mdt[j][i]="";}
        ala = new String[10][2];
        mntc = mdtc = alac = c_mdt = c_ala = 0;
        b = false;
    }

    public void read(String ifile) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(ifile))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] token = line.split("\\s+");
                if (!token[0].equals("null") && a == 1) {
                    mnt[mntc][0] = token[0];
                    mnt[mntc][1] = token[1];
                    mnt[mntc][2] = token[2];
                    mnt[mntc][3] = token[3];
                    mntc++;
                } else if (!token[0].equals("null") && a == 2) {
                    mdt[mdtc][0] = token[0];
                    mdt[mdtc][1] = token[1];
                    if (token.length==3){
                    mdt[mdtc][2] = token[2];}
                    mdtc++;
                } else if (!token[0].equals("null") && a == 3) {
                    ala[alac][0] = token[0];
                    ala[alac][1] = token[1];
                    alac++;
                } else {
                    a++;
                }
            }
        }
    }

    public void macro(String ifile, String ofile) throws IOException {
        try (BufferedReader R = new BufferedReader(new FileReader(ifile));
             FileWriter w = new FileWriter(ofile)) {

            String line;
            while ((line = R.readLine()) != null) {
                String[] token = line.split("\\s+");
                if (token[0].equals("START"))
                    b = true;

                if (b) {
                    int x = -1;
                    for (int i = 0; i < mntc; i++) {
                        if (token[0].equals(mnt[i][1])) {
                            c_mdt = Integer.parseInt(mnt[i][2]);
                            c_ala = Integer.parseInt(mnt[i][3]);
                            x = i;
                            break;
                        }
                    }
                    if (x != -1) {
                        String[] arg = token[1].split("\\,");     // token is reference of source code 
                        for (int a = 0; a < arg.length; a++) {
                            ala[c_ala-1][1] = arg[a];
                            System.out.println(ala[a][1]);
                            c_ala++;
                        }
                        while (!mdt[c_mdt-1][1].equals("MEND")) {
                            String t = "";
                            t = macro_replace(mdt[c_mdt-1][2]);

                            w.write(mdt[c_mdt-1][1] + " " + t); 
                            w.write(System.lineSeparator());
                            c_mdt++;
                        }
                    } else {
                        w.write(line);
                        w.write(System.lineSeparator());
                    }
                }
            }
        }
    }

    private String macro_replace(String x) {
    String[] aa = x.split(",");
    StringBuilder ab = new StringBuilder();
    int j;
    for (String s : aa) {
        j = Integer.parseInt(s.substring(1))-1;
        //System.out.println(j); // Get the index from the argument
        if (j >= 0 && j < ala.length) {
            ab.append(ala[j][1]); // Append the replacement value
        }
        ab.append(","); // Append a comma for the next argument
    }
    return ab.toString();
}


    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("pass2_m.java <input.txt> <Datafile.txt> <output.txt>");
            return;
        }

        pass2_m pass2 = new pass2_m();
        try {
            pass2.read(args[1]);
            pass2.macro(args[0], args[2]);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
