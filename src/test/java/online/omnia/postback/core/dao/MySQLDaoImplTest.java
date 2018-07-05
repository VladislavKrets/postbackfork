package online.omnia.postback.core.dao;

import online.omnia.postback.core.trackers.entities.CurrencyEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by lollipop on 27.03.2018.
 */
public class MySQLDaoImplTest {
    private MySQLDaoImpl mySQLDao;

    @Before
    public void initTest() {
        mySQLDao = MySQLDaoImpl.getInstance();
    }
    @Test
    public void get780StatusTest() {
        String st = mySQLDao.get780Status("S_approved", "Clickdealer");
        assertEquals("approved", st);

    }
    @Test
    public void getCurrencyTest() {
        CurrencyEntity currencyEntity = mySQLDao.getCurrency("cad");
        assertNotNull(currencyEntity);

    }
    @After
    public void afterTest() {
        MySQLDaoImpl.getMasterDbSessionFactory().close();
        mySQLDao = null;
    }
}
