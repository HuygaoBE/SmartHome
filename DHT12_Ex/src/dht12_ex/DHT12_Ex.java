package dht12_ex;

import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.util.ArrayList;
/**
 *
 * @author Dell
 */
public class DHT12_Ex {

    private static I2CDevice   dev = null;
    private static float nhietdo;
    private static int nhietdoint;
    private static float doam;
    static boolean x =false;
	public static void main(String[] args) throws Exception{
            System.out.println("Project Nien luan co so: He thong giam sat nhiet do - do am trong nha");
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1); //
            
            final GpioController gpio = GpioFactory.getInstance();

            // provision gpio pin #01 as an output pin and turn on
            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.HIGH);

            // set shutdown state for this pin
            pin.setShutdownOptions(true, PinState.LOW);

        while(true){
            dev = bus.getDevice(0x5c);
            byte data[] = new byte[5];
            dev.read(0x00,data, 0,5);
            if(x){
                    System.out.println(data[0]); // Gia tri Do am hang chuc
                    System.out.println(data[1]); // Gia tri Do am hang thap phan
                    System.out.println(data[2]); // Gia tri Nhiet do hang chuc
                    System.out.println(data[3]); // Gia tri Nhiet do hang thap phan
                    System.out.println(data[4]); // Day du lieu = Nhiet do + Do am
            }

            if(data[4]==(data[0]+data[1]+data[2]+data[3])){

                    double doam_=Double.parseDouble(data[0]+"."+data[1]);
                    double nhietdo_=Double.parseDouble(data[2]+"."+data[3]);
                    doam = (float)doam_;
                    nhietdo = (float)nhietdo_; 
                    nhietdoint = (int)nhietdo;
                    System.out.println("Nhiet Do : "+ nhietdo +" °C " + "--- Do am : "+ doam + " %");
                    
            }
            else
                    System.out.println("Khong nhan duoc Data");
            //----------------------------------------------------
            
        /*    
            if(nhietdoint > 10 | nhietdoint<=0){
                try{
                  System.out.println("Canh bao!! Nhiet do vuot qua nguong cho phep");
                pin.high();
                Thread.sleep(15000);
            // turn off gpio pin #01
                pin.low();
                Thread.sleep(15000);
                }//close try
                catch(Exception e){
                    System.out.println("Loi nhap xuat!");
                }
            }
        */       
        
            
            //-----------------------Ham tra ve nhiet do, do am, ngay, gio -----------------------------
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String ngaygio = formatter.format(date);
            int len = ngaygio.length();
            //System.out.println("Ngày đã được định dạng : "+ngaygio+"Do dai: "+len);
            int z = ngaygio.indexOf(" ");
            String ngay = ngaygio.substring(0,z);
            String gio = ngaygio.substring(z+1,z+3);
            System.out.println("Ngay: "+ ngay + "--Gio: " + gio);
            int gioint = Integer.parseInt(gio);         
            
            
            
//------------------------------------------------------------------------
        FileReader fr = new FileReader("/var/www/html/filenhietdo.txt");
        BufferedReader br = new BufferedReader(fr);
        

       ArrayList<String> result = new ArrayList<>();
       
         while (br.ready()) {
            result.add(br.readLine());
            
        }
         br.close();
        fr.close();
        
        // Ghi file null
        PrintWriter fwdel = new PrintWriter("/var/www/html/filenhietdo.txt");
        fwdel.print("");
        fwdel.close();
        
        //--------------------------------------------------
        
        FileReader fr2 = new FileReader("/var/www/html/filedoam.txt");
            BufferedReader br2 = new BufferedReader(fr2);
            
            ArrayList<String> result2 = new ArrayList<>();
            
            while (br2.ready()) {
                result2.add(br2.readLine());
            
            }//close while
            br2.close();
            fr2.close();
            
            PrintWriter fwdel2 = new PrintWriter("/var/www/html/filedoam.txt");
            fwdel2.print("");
            fwdel2.close();
            
         //----------------------------------
         //Ghi file
         FileWriter fw = new FileWriter("/var/www/html/filenhietdo.txt",true);
         FileWriter fw2 = new FileWriter("/var/www/html/filedoam.txt",true);
         
        if(nhietdoint > 40 | nhietdoint<=0){
                try{
                  System.out.println("Canh bao!! Nhiet do vuot qua nguong cho phep");
                pin.high();
                Thread.sleep(15000);
            // turn off gpio pin #01
                pin.low();
                Thread.sleep(15000);
                }//close try
                catch(Exception e){
                    System.out.println("Loi nhap xuat!");
                }
            } 
//---------- Dieu kien gio = 0 --------------result1,2[0] co gia tri (nhietdo,doam); con result1,2[1]->result1,2[11] co gia tri 0 ------------------------------
        
        if((gioint==0)){
                try{
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    
                    for(int l=1; l<24; l++){
                        fw.write("" + 0 + "\n");
                        fw2.write("" + 0 + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
//--------------------- Dieu kien gio = 1 -----------------------------------------------------
        else if((gioint==1)){
                try{
                    for(int l=0; l<1; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=2; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//---------------------- Dieu kien gio = 2 ----------------------------------------------------
        else if((gioint==2)){
                try{
                    for(int l=0; l<2; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=3; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if

//---------------------------- Dieu kien gio = 3 ----------------------------------------------
        else if((gioint==3)){
                try{
                    for(int l=0; l<3; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=4; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if   
        
//------------------------------ Dieu kien gio = 4 --------------------------------------------
        else if((gioint==4)){
                try{
                    for(int l=0; l<4; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=5; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------ Dieu kien gio = 5 --------------------------------------------
        else if((gioint==5)){
                try{
                    for(int l=0; l<5; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=6; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------ Dieu kien gio 6 --------------------------------------------
        else if((gioint==6)){
                try{
                    for(int l=0; l<6; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=7; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//---------------------------- Dieu kien gio = 7 ----------------------------------------------
        else if((gioint==7)){
                try{
                    for(int l=0; l<7; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=8; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//-------------------------- Dieu kien gio = 8 ------------------------------------------------
        else if((gioint==8)){
                try{
                    for(int l=0; l<8; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=9; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------- Dieu kien gio = 9 -------------------------------------------
        else if((gioint==9)){
                try{
                    for(int l=0; l<9; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=10; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//----------------------------- Dieu kien gio = 10 ---------------------------------------------
        else if((gioint==10)){
                try{
                    for(int l=0; l<10; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=11; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//----------------------------- Dieu kien gio = 11 ---------------------------------------------
        else if((gioint==11)){
                try{
                    for(int l=0; l<11; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=12; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//----------------------------------- Dieu kien gio = 12 --------------------------------------------
    
        else if((gioint==12)){
                try{
                    for(int l=0; l<12; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=13; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//-------------------------------------------- Dieu kien gio = 13 -------------------------

        else if((gioint==13)){
                try{
                    for(int l=0; l<13; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=14; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if

//------------------------------Dieu kien gio = 14 ----------------------------

        else if((gioint==14)){
                try{
                    for(int l=0; l<14; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=15; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 15 ----------------------------

        else if((gioint==15)){
                try{
                    for(int l=0; l<15; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=16; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if

//------------------------------Dieu kien gio = 16 ----------------------------

        else if((gioint==16)){
                try{
                    for(int l=0; l<16; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=17; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if   
        
//------------------------------Dieu kien gio = 17 ----------------------------

        else if((gioint==17)){
                try{
                    for(int l=0; l<17; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=18; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 18 ----------------------------

        else if((gioint==18)){
                try{
                    for(int l=0; l<18; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=19; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 19 ----------------------------

        else if((gioint==19)){
                try{
                    for(int l=0; l<19; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=20; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 20 ----------------------------

        else if((gioint==20)){
                try{
                    for(int l=0; l<20; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=21; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 21 ----------------------------

        else if((gioint==21)){
                try{
                    for(int l=0; l<21; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=22; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 22 ----------------------------

        else if((gioint==22)){
                try{
                    for(int l=0; l<22; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    for(int k=23; k<24; k++){
                        fw.write("" + result.get(k) + "\n");
                        fw2.write("" + result2.get(k) + "\n");
                    }//close for
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
        
//------------------------------Dieu kien gio = 23 ----------------------------

        else if((gioint==23)){
                try{
                    for(int l=0; l<23; l++){
                        fw.write("" + result.get(l) + "\n");
                        fw2.write("" + result2.get(l) + "\n");
                    }//close for
                    
                    fw.write("" + nhietdo + "\n");
                    fw2.write("" + doam + "\n");
                    fw.close();
                    fw2.close();
               }//close try
                catch(Exception e){
                    System.out.println("Loi ghi file!");
                }
            }//close else if
//-----------------------------Delay Thoi Gian--------------------------------
            System.out.println("\n");
            Thread.sleep(30000);// delay 30s
            //Thread.sleep(3600000);//delay 1 gio`
        }//close while
	}//close main
    
}//close class