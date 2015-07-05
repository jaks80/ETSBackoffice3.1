package com.ets.accountingdoc.service;

import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.air.service.AirService;
import com.ets.mockdata.MockPnrData;
import com.ets.mockdata.MockSalesAcDoc;
import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Yusuf
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
@Transactional
public class TSalesAcDocServiceTest {

    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    private TSalesAcDocService tSalesAcDocService;
    @Autowired
    private AirService airService;

    public TSalesAcDocServiceTest() {
    }

    @Before
    public void openSession() {
        currentSession = sessionFactory.getCurrentSession();

    }

    @Test
    public void shouldHaveASessionFactory() {
        assertNotNull(sessionFactory);
    }

    @Test
    public void shouldHaveNoObjectsAtStart() {
        List<?> results = currentSession.createQuery("from TicketingSalesAcDoc").list();
        assertTrue(results.isEmpty());
    }

    //@Test
    public void testCreateNewDocument() {
        System.out.println("New Dcoment Test");
        Pnr pnr = savePnrData();

        MockSalesAcDoc mockDoc = new MockSalesAcDoc();
        TicketingSalesAcDoc newInvoice = mockDoc.getInvoice();
        newInvoice.setTickets(pnr.getTickets());
        newInvoice.setPnr(pnr);

        newInvoice.setDocumentedAmount(newInvoice.calculateTicketedSubTotal().
                add(newInvoice.calculateAddChargesSubTotal().
                        add(newInvoice.calculateAddChargesSubTotal())));

        TicketingSalesAcDoc result = tSalesAcDocService.newDocument(newInvoice);

        assertEquals("784.20", result.getDocumentedAmount().toString());
        assertEquals("1001", result.getReference().toString());
        assertNotNull(result.getId());

        for (Ticket t : newInvoice.getTickets()) {
            assertNotNull(t.getId());            
        }

//        Pnr pnrInDb = airService.findPnr(pnr.getGdsPnr(), pnr.getPnrCreationDate());
//                
//        MockPnrData mockData = new MockPnrData();
//        Set<Ticket> tickets = mockData.getMockRefundTickets();
//        
//        airService.refundTicket(new ArrayList(tickets));
//
//        TicketingSalesAcDoc cnote = mockDoc.getCreditNote();
//        cnote.setAcDocRef(newInvoice.getAcDocRef());
//        cnote.setPnr(pnr);
//        //cnote.setTickets(refundTickets);
//        cnote.setDocumentedAmount(cnote.calculateTicketedSubTotal().
//                add(cnote.calculateAddChargesSubTotal().
//                        add(cnote.calculateOtherServiceSubTotal())));
//
//        TicketingSalesAcDoc persistedCNote = tSalesAcDocService.newDocument(cnote);
//
//        for (Ticket t : persistedCNote.getTickets()) {
//            assertNotNull(t.getId());
//        }
//        assertEquals("287.10", result.getDocumentedAmount().toString());
//        assertNotNull(persistedCNote.getId());
    }

    //@Test
    public void testGetByReffference() {
        System.out.println("getByReffference");
        int refNo = 0;
        TSalesAcDocService instance = new TSalesAcDocService();
        List<TicketingSalesAcDoc> expResult = null;
        List<TicketingSalesAcDoc> result = instance.getByReffference(refNo);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testGetByGDSPnr() {
        System.out.println("getByGDSPnr");
        String pnr = "";
        TSalesAcDocService instance = new TSalesAcDocService();
        List<TicketingSalesAcDoc> expResult = null;
        List<TicketingSalesAcDoc> result = instance.getByGDSPnr(pnr);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testGetOutstandingInvoice() {
        System.out.println("getOutstandingInvoice");
        Long contactableId = null;
        int type = 0;
        Date from = null;
        Date to = null;
        TSalesAcDocService instance = new TSalesAcDocService();
        List<TicketingSalesAcDoc> expResult = null;
        //List<TicketingSalesAcDoc> result = instance.getOutstandingInvoice(contactableId, type, from, to);
        //assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testInvoiceHistoryByCriteria() {
        System.out.println("invoiceHistoryByCriteria");
        Long contactableId = null;
        int contactableType = 0;
        Integer docTypeFrom = null;
        Integer docTypeTo = null;
        Date from = null;
        Date to = null;
        Long tktingAgtFrom = null;
        Long tktingAgtTo = null;
        TSalesAcDocService instance = new TSalesAcDocService();
        List<TicketingSalesAcDoc> expResult = null;
        //List<TicketingSalesAcDoc> result = instance.invoiceHistoryByCriteria(contactableId, contactableType, docTypeFrom, docTypeTo, from, to, tktingAgtFrom, tktingAgtTo);
        //assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    private Pnr savePnrData() {
        MockPnrData mockData = new MockPnrData();
        Pnr issuedPnr = mockData.getMockPnr();
        Set<Ticket> issuedTickets = mockData.getMockTTPIssuedTickets();
        for(Ticket t: issuedTickets){
         t.setPnr(issuedPnr);
        }
        
        Set<Itinerary> issuedSegments = mockData.getMockSegments();
        issuedPnr.setTickets(issuedTickets);
        issuedPnr.setSegments(issuedSegments);

        airService.savePnr(issuedPnr,"");
        return issuedPnr;
    }

}
