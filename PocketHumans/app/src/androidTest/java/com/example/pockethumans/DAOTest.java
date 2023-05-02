package com.example.pockethumans;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

import static org.hamcrest.CoreMatchers.is;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.pockethumans.DB.HumanDAO;
import com.example.pockethumans.DB.MovesDAO;
import com.example.pockethumans.DB.UserLoginDAO;
import com.example.pockethumans.DB.TestDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DAOTest {
    private UserLoginDAO userDao;
    private TestDatabase userDB;
    private MovesDAO movesDao;
    private TestDatabase movesDB;
    private HumanDAO humanDao;
    private TestDatabase humanDB;
    Human testHuman1 = new Human("William","A mustached student at CSUMB. It's said he loves his boyfriend, his cat, and the color yellow. He's known for his incredible wit and humility.",
            30,30,30,3,11,12,15);
    Human testHuman2 = new Human ("Ben","A handsome software developer at Apple. William's dashing boyfriend. Gregarious and funny, he loves conversation. And also pickles, apparently.",
            25,40,25,5,10,12,14);
    Move testMove1 = new Move("Tackle", "User tackles the opponent, damaging them.", 25, "attack");
    Move testMove2 = new Move("Headbutt", "User headbutts the opponent with abandon, damaging them.", 30, "attack");
    User testUser1 = new User("testuser1", "testuser1", false);
    User testUser2 = new User("admin2", "admin2", true);

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        userDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        userDao = userDB.UserLoginDAO();
        movesDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        movesDao = movesDB.MovesDAO();
        humanDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        humanDao = humanDB.HumanDAO();
    }

    @After
    public void closeDb() throws IOException {
        userDB.close();
        movesDB.close();
        humanDB.close();
    }

    @Test
    public void insertUserTest() throws Exception {
        assertThat(userDao.listUsers().size(),is (0));
        userDao.insert(testUser1);
        assertThat(userDao.checkLogin(testUser1.getUsername(),testUser1.getPassword()).getUsername(),is (testUser1.getUsername()));
        assertThat(userDao.listUsers().size(),is (1));
    }

    @Test
    public void insertHumanTest() throws Exception {
        assertThat(humanDao.listHumans().size(),is(0));
        humanDao.insert(testHuman1);
        assertThat(humanDao.listHumans().size(),is(1));
        assertThat(humanDao.getHuman(testHuman1.getName()).getName(),is(testHuman1.getName()));
    }

    @Test
    public void insertMoveTest() throws Exception {
        assertThat(movesDao.listMoves().size(),is(0));
        movesDao.insert(testMove1);
        assertThat(movesDao.listMoves().size(),is(1));
        assertThat(movesDao.getMove(testMove1.getName()).getName(),is(testMove1.getName()));
    }
}
