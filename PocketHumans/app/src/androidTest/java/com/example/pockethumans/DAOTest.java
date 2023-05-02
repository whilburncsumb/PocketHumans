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

public class DAOTest {
    private UserLoginDAO userDao;
    private TestDatabase userDB;
    private MovesDAO movesDao;
    private TestDatabase movesDB;
    private HumanDAO humanDao;
    private TestDatabase humanDB;
    Human testHuman1 = new Human("William","A mustached student at CSUMB. It's said he loves his boyfriend, his cat, and the color yellow. He's known for his incredible wit and humility.",
            30,30,30,3,11,12,15);
    Move testMove1 = new Move("Tackle", "User tackles the opponent, damaging them.", 25, "attack");
    User testUser1 = new User("testuser1", "testuser1", false);


    @Before
    public void createDb() {
        testUser1.setUserId(1);
        testMove1.setMoveId(1);
        testHuman1.setHumanId(1);
        Context context = ApplicationProvider.getApplicationContext();
        userDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        userDao = userDB.UserLoginDAO();
        movesDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        movesDao = movesDB.MovesDAO();
        humanDB = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        humanDao = humanDB.HumanDAO();
    }

    @After
    public void closeDb() {
        userDB.close();
        movesDB.close();
        humanDB.close();
    }

    @Test
    public void insertUserTest() {
        assertThat(userDao.listUsers().size(),is (0));
        userDao.insert(testUser1);
        assertThat(userDao.checkLogin(testUser1.getUsername(),testUser1.getPassword()),is (testUser1));
        assertThat(userDao.listUsers().size(),is (1));
    }

    @Test
    public void insertHumanTest() {
        assertThat(humanDao.listHumans().size(),is(0));
        humanDao.insert(testHuman1);
        assertThat(humanDao.listHumans().size(),is(1));
        assertThat(humanDao.getHuman(testHuman1.getName()).getName(),is(testHuman1.getName()));
    }

    @Test
    public void insertMoveTest() {
        assertThat(movesDao.listMoves().size(),is(0));
        movesDao.insert(testMove1);
        assertThat(movesDao.listMoves().size(),is(1));
        assertThat(movesDao.getMove(testMove1.getName()).getName(),is(testMove1.getName()));
    }

    @Test
    public void updateUserTest(){
        userDao.insert(testUser1);
        assertThat(userDao.checkLogin(testUser1.getUsername(),testUser1.getPassword()).getUsername(),is (testUser1.getUsername()));
        String newValue = "Altered Value";
        testUser1.setUsername(newValue);
        userDao.update(testUser1);
        assertThat(userDao.checkLogin(newValue,testUser1.getPassword()),is (testUser1));
        assertThat(userDao.listUsers().size(),is (1));
    }

    @Test
    public void updateHumanTest(){
        humanDao.insert(testHuman1);
        assertThat(humanDao.listHumans().size(),is(1));
        String newValue = "Altered Value";
        testHuman1.setName(newValue);
        humanDao.update(testHuman1);
        assertThat(humanDao.getHuman(newValue).getName(),is(testHuman1.getName()));
        assertThat(humanDao.listHumans().size(),is(1));
    }

    @Test
    public void updateMoveTest(){
        movesDao.insert(testMove1);
        assertThat(movesDao.listMoves().size(),is(1));
        String newValue = "Altered Value";
        testMove1.setName(newValue);
        movesDao.update(testMove1);
        assertThat(movesDao.getMove(newValue).getName(),is(testMove1.getName()));
        assertThat(movesDao.listMoves().size(),is(1));
    }

    @Test
    public void deleteUserTest(){
        userDao.insert(testUser1);
        assertThat(userDao.checkLogin(testUser1.getUsername(),testUser1.getPassword()).getUsername(),is (testUser1.getUsername()));
        assertThat(userDao.listUsers().size(),is (1));
        userDao.delete(testUser1);
        assertThat(userDao.listUsers().size(),is (0));
    }

    @Test
    public void deleteMoveTest(){
        movesDao.insert(testMove1);
        assertThat(movesDao.getMove(testMove1.getName()).getName(),is(testMove1.getName()));
        assertThat(movesDao.listMoves().size(),is (1));
        movesDao.delete(testMove1);
        assertThat(movesDao.listMoves().size(),is (0));
    }

    @Test
    public void deleteHumanTest(){
        humanDao.insert(testHuman1);
        assertThat(humanDao.getHuman(testHuman1.getName()).getName(),is(testHuman1.getName()));
        assertThat(humanDao.listHumans().size(),is (1));
        humanDao.delete(testHuman1);
        assertThat(humanDao.listHumans().size(),is (0));
    }
}
