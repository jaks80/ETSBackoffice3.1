//package com.ets.service.pnr;
//
//import com.ets.pnr.service.PnrService;
//import com.ets.pnr.domain.Itinerary;
//import com.ets.pnr.domain.Pnr;
//import com.ets.pnr.domain.Ticket;
//import com.ets.mockdata.MockPnrData;
//import com.ets.air.service.AirService;
//import com.ets.util.Enums;
//import com.ets.util.Enums.TicketStatus;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import javax.transaction.Transactional;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// *
// * @author Yusuf https://gist.github.com/twasink/2881461 testContext.xml is in
// * "Other Sources"
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testContext.xml")
//@Transactional
//public class PnrServiceTest {
//
//    @Autowired
//    private SessionFactory sessionFactory;
//    private Session currentSession;
//
//    @Autowired
//    private AirService airService;
//
//    @Before
//    public void openSession() {
//        currentSession = sessionFactory.getCurrentSession();
//    }
//
//    @Test
//    public void shouldHaveASessionFactory() {
//        assertNotNull(sessionFactory);
//    }
//
//    @Test
//    public void shouldHaveNoObjectsAtStart() {
//        List<?> results = currentSession.createQuery("from Pnr").list();
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    public void testBooking() {
//        System.out.println("New booking test...");
//
//        MockPnrData mockData = new MockPnrData();
//
//        Pnr bookedPnr = mockData.getMockPnr();
//        bookedPnr.setTicketingAgtOid(null);
//
//        Set<Ticket> bookedTickets = mockData.getMockBookedTickets();
//        Set<Itinerary> bookedSegments = mockData.getMockSegments();
//        bookedPnr.setTickets(bookedTickets);
//        bookedPnr.setSegments(bookedSegments);
//
//        airService.savePnr(bookedPnr);        
//
//        Pnr persistedPnr = airService.findPnr(bookedPnr.getGdsPnr(), bookedPnr.getPnrCreationDate());
//        assertNotNull(persistedPnr);
//        assertNull(persistedPnr.getTicketingAgtOid());        
//
//        for (Ticket t : persistedPnr.getTickets()) {
//            assertNotNull(t.getId());
//        }       
//
//        List<Long> segIds = new ArrayList<>();
//        for (Itinerary t : persistedPnr.getSegments()) {
//            if (t.getId() != null || t.getId() != 0) {
//                segIds.add(t.getId());
//            }
//        }
//
//        assertEquals(4, segIds.size());
//
//    }
//
//    @Test
//    public void testIssue() {
//        
//        System.out.println("Issue test...");
//        MockPnrData mockData = new MockPnrData();
//        Pnr issuedPnr = mockData.getMockPnr();
//        Set<Ticket> issuedTickets = mockData.getMockTTPIssuedTickets();
//        Set<Itinerary> issuedSegments = mockData.getMockSegments();
//        issuedPnr.setTickets(issuedTickets);
//        issuedPnr.setSegments(issuedSegments);
//
//        airService.savePnr(issuedPnr);        
//        assertEquals("LONU123IT", issuedPnr.getTicketingAgtOid());
//        assertNotNull(issuedPnr);
//       
//        for (Ticket t : issuedPnr.getTickets()) {
//            assertEquals(TicketStatus.ISSUE, t.getTktStatus());
//            assertNotNull(t.getId());
//            assertNotNull(t.getTicketNo());                        
//        }
//
//        for (Itinerary i : issuedPnr.getSegments()) {
//            assertNotNull(i.getId());
//        }
//    }
//
//    //@Test
//
//    public void testGetByGDSPnr() throws Exception {
//        System.out.println("getByGDSPnr");
//        String gdsPnr = "";
//        PnrService instance = new PnrService();
//        Pnr expResult = null;
//        Pnr result = instance.getByGDSPnr(gdsPnr);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }   
//}
