/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uredjaj.korisnickiuredjaj;

import clients.Helper;
import clients.ResponseErrorCode;
import clients.RestClient;
import clients.UserClient;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import responses.Activity;
import responses.ActivityList;
import responses.Alarm;
import responses.AlarmList;
import responses.Distance;
import responses.Moment;
import responses.Song;
import responses.SongList;
import responses.XmlMomentList;
import retrofit2.Retrofit;



/**
 *
 * @author user2
 */
public class Main {
    public static final String NOT_LOGGED_IN = "Niste prijavljeni";
    public static final String OPERATION_NOT_SUCCEED = "Neuspesno izvrsavanje operacije";
    public static final String CONNECTION_NOT_SUCCEED = "Neuspela konekcija na server";
    public static final String INTERNAL_SERVER_ERROR = "Greska na serveru";
    public static final String OPERATION_SUCCEED = "Operacija uspesno izvresena";
    
    Scanner scanner = new Scanner(System.in);
    private RestClient rc = null;
    
    public static void main(String[] args)  {
        
        Main main = new Main();
        printMainMenu();

        while (true) {
            System.out.println("Unesite redni broj operacije: ");
            int command;
            try {
                command = Integer.parseInt(main.scanner.nextLine()); // ;
            } catch (Exception ex) {
                command = -1;
            }
            
            switch (command) {
                case 0:
                    return;
                case 1:
                    main.register();
                    break;
                case 2:
                    main.login();
                    break;
                case 3:
                    main.playSong();
                    break;
                case 4:
                    main.songList();
                    break;
                case 5:
                    main.playedSongList();
                    break;
                case 6:
                    main.createAlarm();
                    break;
                case 7:
                    main.momentAlarm();
                    break;
                case 8:
                    main.setAlarmSong();
                    break;
                case 9:
                    main.getAlarmList();
                    break;
                case 10:
                    main.distanceBetween();
                    break;
                case 11:
                    main.getDistanceCurrent();
                    break;
                case 12:
                    main.createActivity();
                    break;
                case 13:
                    main.getActivityList();
                    break;
                case 14:
                    main.deleteAcivity();
                    break;
                case 15:
                    main.updateActivity();
                    break;
                default:
                    System.out.println("Nepostojeca operacija.");
                    break;
            }
            
        }
    }
    static void  printMainMenu() {
        String[] commands = {
            "Iskljuci program",
            "Registruj se",
            "Prijavi se",
            "Pusti muziku",
            "Spisak pesama",
            "Spisak odslusanih pesama",
            "Kreiraj alarm",
            "Izaberi od ponudjenih alarma",
            "Podesi pesmu alarma",
            "Spisak navijenih alarma", 
            "Rastojanje izmedju dve lokacije",
            "Rastojanje od trenutne lokacije do izabrane lokacije",
            "Kreiraj obavezu",
            "Spisak obaveza",
            "Obrisi obavezu",
            "Azuriraj obavezu"
        };
        for (int i = 0; i < commands.length; i++) {
            System.out.println("(" + i + ") " +  commands[i]);
        }
    
    }
    
    
    private void register() {
        
        Credentials c = readCredentials();
        System.out.println("Unesite adresu kuće: ");
        String homeLocation = scanner.nextLine();
        //System.out.println(c.getUsername() + "; " + c.getPassword() + "; " + homeLocation );
        try {
            String register = UserClient.register(homeLocation, homeLocation, homeLocation);
            this.rc = new RestClient(c.getUsername(), c.getPassword());
            System.out.println("Uspesno registrovanje i prijavljivanje na server");
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode rec) {
            int code = rec.getCode();
            if (code == 409) {
                System.out.println("Korisnicko ime vec zauzeto");
            } else if (code == 400) {
                System.out.println("Unesite sve potrebne parametre");
            } else if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            System.out.println(OPERATION_NOT_SUCCEED);
        } 
        
    }
    private Credentials readCredentials() {
        System.out.println("Unesite ime korisnika: ");
        String username = scanner.nextLine();
        System.out.println("Unesite lozinku: ");
        String password = scanner.nextLine();
        return new Credentials(username, password);
    }
    private void login() {
        Credentials c = readCredentials();
        try {
            String loginStatus = UserClient.login(c.getUsername(), c.getPassword());
            System.out.println("Uspesno prijavljivanje");
            this.rc = new RestClient(c.getUsername(), c.getPassword());
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode rec) {
            int code = rec.getCode();
            System.out.println(code);
            if (code == 406) {
                System.out.println("Kredencijali nisu ispravni");
            } else if (code == 500) {
                System.out.println("Greska na serveru");
            }
        } catch (Exception e){
            System.out.println(OPERATION_NOT_SUCCEED);
        }
                
        
    }
    void songList() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        try {
            SongList songList = rc.getSongList();
            System.out.println("Spisak pesama: ");
            printSongList(songList);
        } catch (Exception e) {
        
        }
    }
    void playedSongList() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        try {
           
            SongList songList = rc.getPlayedSongList();
            System.out.println("Odslusane pesme: ");
            printSongList(songList);
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode erc) {
            int code = erc.getCode();
            if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            }
            //TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    void printSongList(SongList songList) {
        for (Song song : songList.getSongList()) {
            System.out.println("ID: " + song.getIdSong() + ", Pesma: " + song.getName());
        }
    }

    private void playSong() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        System.out.println("Unesite redni broj pesme:");
        int idSong = scanner.nextInt();
        scanner.nextLine();
        try {
            Song playSong = rc.playSong(idSong);
            System.out.println("Pesma: " + playSong.getName());
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode erc) {
            int code = erc.getCode();
            if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            }
            //TODO
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
        }
    }

    private void getAlarmList() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        try {
            AlarmList alarmList = rc.getAlarmList();
            if (alarmList == null) {
                System.out.println("Spisak alarma je prazan.");
            } else {
                System.out.println("Spisak alarma: ");
                printAlarmList(alarmList);
            }
            
            
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode erc) {
        
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(OPERATION_NOT_SUCCEED);
        }
    }

    private void printAlarmList(AlarmList alarmList) {
        
        for (Alarm alarm: alarmList.getAlarmList()) {
            System.out.println(alarm.toText());
        }
    }
    

    private void momentAlarm() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        try {
            XmlMomentList momentList = rc.getMomentList();
            if (momentList != null) {
                for (Moment moment : momentList.getMomentList()) {
                    System.out.println(moment);
                }
            }
            System.out.println("Redni broj: ");
            Integer idMoment = scanner.nextInt();
            scanner.nextLine();
            if (idMoment < 0 || idMoment >= momentList.getMomentList().size()) {
                System.out.println("Redni broj je van opsega");
                return;
            }
            rc.postMomentAlarm(idMoment);
            System.out.println("Uspesno dodavanje alarma");
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(OPERATION_NOT_SUCCEED);
        }
        
        
    }
    void distanceBetween() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        System.out.println("Unesite mesto polaska:");
        String origin = scanner.nextLine();
        System.out.println("Unesite mesto dolaska");
        String destination = scanner.nextLine();
        try {
            Distance distanceBetween = rc.getDistanceBetween(origin, destination);
            System.out.println(distanceBetween.toText());
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void getDistanceCurrent() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        System.out.println("Unesite mesto dolaska");
        String destination = scanner.nextLine();
        try {
            Distance distance = rc.getDistanceCurrent(destination);
            System.out.println(distance.toText());
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void getActivityList() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }

        try {
            ActivityList activityList = rc.getActivityList();
            List<Activity> list = activityList.getActivityList();
            if (list == null || list.size() == 0) {
                System.out.println("Spisak obaveza je prazan");
                return;
            }
            for (Activity activity : list) {
                System.out.println(activity.toText());
            }
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    void setAlarmSong() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        System.out.println("Unesite identifikaciju pesme:");
        int idSong = scanner.nextInt();
        scanner.nextLine();
        
        try {
            rc.putAlarmSong(idSong);
            System.out.println("Pesma alarma je uspesno postavljena");
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch(ResponseErrorCode rec) {
            int code = rec.getCode();
            if (code == 400) {
                System.out.println("Ne postoji pesma sa datom identifikacijom");
            } else if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            } else {
                System.out.println(OPERATION_NOT_SUCCEED);
            }
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }
    void deleteAcivity() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        System.out.println("Unesite identifikaciju obaveze:");
        int idActivity = scanner.nextInt();
        scanner.nextLine();
        try {
            rc.deleteActivity(idActivity);
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        } catch (ResponseErrorCode rec) {
            int code = rec.getCode();
            if (code == 400) {
                System.out.println("Ne postoji obaveza sa datom identifikacijom");
            } else if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            } else {
                System.out.println(OPERATION_NOT_SUCCEED);
            }
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    LocalTime readTime() {
        while (true) {
            System.out.println("Unesite vreme u formatu hh:mm:ss");
            String timeString = scanner.nextLine();
            try {
                LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
                return localTime;
            } catch (Exception ex) {
                System.out.println("Loš format");
            }
        }       
    }
    LocalDate readDate() {
        while (true) {
            System.out.println("Unesite datum u formatu dd/MM/yyyy");
            String dateString = scanner.nextLine();
            try {
                LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return localDate;
            } catch (Exception ex) {
                System.out.println("Loš format");
            }
        }
    }
    Activity readActivity() {
        System.out.println("Unesite datum i vreme pocetka obaveze: ");
        LocalDate localDate = readDate();
        LocalTime localTime = readTime();
        System.out.println("Unesite vreme trajanja");
        LocalTime localTimeDuration = readTime();
        LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
        System.out.println("Unesite lokaciju obaveze: ");
        String locationHome = scanner.nextLine();
        if (locationHome.trim().length() == 0) {
            locationHome = null;
            //System.out.println("samo enter");
        }
        Activity activity = new Activity();
        activity.setDestinationName(locationHome);
        activity.setStartDateTime(Helper.fromLocalDateTime(ldt));
        activity.setDurationSeconds(Helper.toSecondsFromLocalTime(localTimeDuration));
        return activity;
    }
    void createActivity() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        Activity activity = readActivity();
        System.out.println("Da li želite da dodate alarm (1/0):");
        int addAlarm = scanner.nextInt();
        scanner.nextLine(); 
        try {
            rc.postActivity(activity, addAlarm == 1);
            System.out.println(OPERATION_SUCCEED);
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        }  catch (ResponseErrorCode rec) {
            int code = rec.getCode();
            if (code == 400) {
                System.out.println("Nije moguće dodati datu obavezu");
            } else if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            } else {
                System.out.println(OPERATION_NOT_SUCCEED);
            }
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void updateActivity() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        int idUpdate;
        System.out.println("Unesite identifikaciju obaveze koju želite da ažurirate: ");
        idUpdate = scanner.nextInt();
        scanner.nextLine();
        Activity activity = readActivity();
        System.out.println("Da li želite da dodate alarm (1/0):");
        int addAlarm = scanner.nextInt();
        scanner.nextLine();
        try {
            rc.updateActivity(activity, addAlarm == 1, idUpdate);
            System.out.println(OPERATION_SUCCEED);
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        }  catch (ResponseErrorCode rec) {
            int code = rec.getCode();
            if (code == 400) {
                System.out.println("Nije moguce izvrsiti azuriranje");
            } else if (code == 500) {
                System.out.println(INTERNAL_SERVER_ERROR);
            } else {
                System.out.println(OPERATION_NOT_SUCCEED);
            }
        } catch (Exception ex) {
            System.out.println(OPERATION_NOT_SUCCEED);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void createAlarm() {
        if (rc == null) {
            System.out.println(NOT_LOGGED_IN);
            return;
        }
        
        
        System.out.println("Unesite datum i vreme alarma: ");
        LocalDate localDate = readDate();
        LocalTime localTime = readTime();
        Integer interval = null;
        System.out.println("Da li želite da se ponavlja posle odredjenog intervala: (0/1))");
        interval = scanner.nextInt();
        scanner.nextLine();
        Integer duration = null;
        if (interval == 1) {
            System.out.println("Unesite vreme trajanja");
            LocalTime localTimeDuration = readTime();   
            duration = Helper.toSecondsFromLocalTime(localTimeDuration);
        }
        
        LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
        Date datetimeMoment = Helper.fromLocalDateTime(ldt);
        Alarm alarm = new Alarm();
        alarm.setIntervalSeconds(interval);
        alarm.setDatetimeMoment(datetimeMoment);
        try {
            rc.postAlarm(alarm);
            System.out.println(OPERATION_SUCCEED);
        } catch (SocketException ex) {
            System.out.println(CONNECTION_NOT_SUCCEED);
        }  catch (Exception ex) {
            System.out.println(INTERNAL_SERVER_ERROR);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
