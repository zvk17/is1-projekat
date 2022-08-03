/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import entities.Alarm;
import entities.OldAlarm;
import entities.Song;
import entities.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;
/**
 *
 * @author user2
 */
public class AlarmWorkingThread extends Thread {
    Topic zvukTopic;
    ConnectionFactory fabrika;

    int idUser;
    String persistenceUnitName = "AlarmPU";
    EntityManagerFactory emf;
    EntityManager em;
    
    public AlarmWorkingThread(Topic zvukTopic, int idUser, ConnectionFactory factory) {
        this.zvukTopic = zvukTopic;
        this.idUser = idUser;
        this.fabrika = factory;
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    @Override
    public void run() {
        System.out.println("alarm.AlarmWorkingThread.run();START");
        try {
            while (true) {
                emf.getCache().evictAll();
                em.clear();
                User user = em.find(User.class, getIdUser());
                Song song = user.getIdSong();

                List<entities.Alarm> alarmList = user.getAlarmList();
                LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
                //System.out.println("Alarm list size: " + alarmList.size());
                for (entities.Alarm alarm : alarmList) {
                    Date date = alarm.getDatetimeMoment();
                    LocalDateTime ldt = toLocalDateTime(date);
                    if (ldt.isBefore(now)) {
                        playSong(user, song);
                        if (alarm.getIntervalSeconds() == null) {
                            System.out.println("alarm.AlarmWorkingThread.run():OLDALARM");
                            moveAlarmToOld(alarm);
                        } else {
                            System.out.println("alarm.AlarmWorkingThread.run():ALARMINTERVAL");
                            updateAlarmMoment(alarm);
                        }                
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
        
            } 
        } finally {
            System.out.println("alarm.AlarmWorkingThread.run();STOP");
        }
        
    }
    
    private void playSong(User user, Song song) {
        System.out.println("alarm.AlarmWorkingThread.playSong(): " + LocalDateTime.now().toString());
        JMSContext ctx = fabrika.createContext();
        JMSProducer producer = ctx.createProducer();
        Message msg = ctx.createMessage();
        try {
            msg.setStringProperty("OPERATION", "PLAY_SONG");
            msg.setIntProperty("ID_SONG", song.getIdSong());
            msg.setIntProperty("ID_USER", idUser);
            producer.send(zvukTopic, msg);
        } catch (Exception ex) {
            Logger.getLogger(AlarmWorkingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        //producer.send(zvukTopic, msg);
    }
   
    private void moveAlarmToOld(Alarm alarm) {
        em.getTransaction().begin();
        OldAlarm oldAlarm = new OldAlarm(alarm);
        em.persist(oldAlarm);
        em.remove(alarm);
        em.getTransaction().commit();
    }

    private void updateAlarmMoment(Alarm alarm) {
        em.getTransaction().begin();
        Date d = alarm.getDatetimeMoment();
        LocalDateTime ldt = toLocalDateTime(d);
        ldt = ldt.plusSeconds(alarm.getIntervalSeconds());        
        d = fromLocalDateTime(ldt);
        alarm.setDatetimeMoment(d);        
        //em.persist(alarm);   
        em.getTransaction().commit();
    }
    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    private Date fromLocalDateTime(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public synchronized void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public synchronized int getIdUser() {
        return this.idUser;
    }
    
}
//konstruktor za old alarm
//public OldAlarm(entities.Alarm alarm) {
//        this.idOldAlarm = alarm.getIdAlarm();
//        this.datetimeMoment = alarm.getDatetimeMoment();
//        this.idUser = alarm.getIdUser();
//    }