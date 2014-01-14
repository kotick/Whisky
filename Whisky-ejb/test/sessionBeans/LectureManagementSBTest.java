/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entity.Course;
import entity.Lecture;
import java.util.Collection;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

/**
 *
 * @author felipe
 */
public class LectureManagementSBTest {

    public LectureManagementSBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createLecture method, of class LectureManagementSB.
     */
    @Test
    public void testCreateLecture() throws Exception {
        System.out.println("createLecture");
        String date = "";
        String timeIni = "";
        String timeFin = "";
        Course Course = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        LectureManagementSBLocal instance = (LectureManagementSBLocal) container.getContext().lookup("java:global/classes/LectureManagementSB");
        Long expResult = null;
        Long result = instance.createLecture(date, timeIni, timeFin, Course);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}