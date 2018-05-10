package cn.edu.ncu.football.ways;

import cn.edu.ncu.football.repo.PlayerRepo;
import cn.edu.ncu.football.repo.RaceRepo;
import cn.edu.ncu.football.repo.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.text.DateFormat;
import javax.xml.crypto.Data;

@Component
public class Match{
    private final PlayerRepo playerRepo;

    private final RaceRepo raceRepo;

    private final TeamRepo teamRepo;

    @Autowired
    public Match(PlayerRepo playerRepo, RaceRepo raceRepo, TeamRepo teamRepo) {
        this.playerRepo = playerRepo;
        this.raceRepo = raceRepo;
        this.teamRepo = teamRepo;
    }

    public class SetDate{
        public Date setDate(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String txtToday = "2018-04-01";
            try {
                Date date = dateFormat.parse(txtToday);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        }
        public  Date addDay(Date date){
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_MONTH, +1);
            Date date1 = rightNow.getTime();
            return date1;
        }
    }
    public void match(){
        SetDate modelDay=new SetDate();
        Date today=modelDay.setDate();
        Date matchDay;
      for(int i=1;i<31;i++){

      }
    }

}
