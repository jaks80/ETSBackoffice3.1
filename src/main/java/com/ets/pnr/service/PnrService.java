package com.ets.pnr.service;

import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.accountingdoc.service.TPurchaseAcDocService;
import com.ets.accountingdoc.service.TSalesAcDocService;
import com.ets.client.domain.MainAgent;
import com.ets.exception.PastSegmentException;
import com.ets.exception.ValidAccountingDocumentExistException;
import com.ets.exception.ValidTicketExist;
import com.ets.pnr.model.collection.Pnrs;
import com.ets.pnr.dao.PnrDAO;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.logic.PnrBusinessLogic;
import com.ets.pnr.logic.PnrDeleteLogic;
import com.ets.pnr.model.ATOLCertificate;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.DateUtil;
import com.ets.pnr.logic.PnrUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("pnrService")
public class PnrService {

    @Resource(name = "pnrDAO")
    private PnrDAO dao;
    @Resource(name = "pnrDeleteLogic")
    private PnrDeleteLogic pnrDeleteLogic;
    @Autowired
    private RemarkService remarkService;
    @Resource(name = "tSalesAcDocService")
    private TSalesAcDocService tSalesAcDocService;
    @Resource(name = "tPurchaseAcDocService")
    private TPurchaseAcDocService tPurchaseAcDocService;

    public void updatePnrSegmentAndLeadPax(String issueDateFrom, String issueDateTo) {

        Date dateFrom = DateUtil.stringToDate(issueDateFrom, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(issueDateTo, "ddMMMyyyy");

        List<Pnr> pnrList = dao.find(dateFrom, dateTo, null, null);
        System.out.println("Found:" + pnrList.size());
        int i = 0;
        for (Pnr pnr : pnrList) {
            String segment = PnrBusinessLogic.getFirstSegmentSummery(pnr.getSegments());
            String leadPax = PnrBusinessLogic.calculateLeadPaxName(pnr.getTickets());
            int status = dao.updatePnrSegmentAndLeadPax(segment, leadPax, pnr.getId());
            i++;
            System.out.println("Updated:" + i);
        }
    }

    public void save(Pnr pnr) {
        PnrUtil.initPnrChildren(pnr);
        dao.save(pnr);
        PnrUtil.undefinePnrChildren(pnr);
    }

    public Set<String> findTicketingOIDs() {
        return dao.findTicketingOIDs();
    }

    /**
     * Thread safety needed
     *
     * @param id
     * @param todayFromUsersMachine
     * @return
     * @throws com.ets.exception.ValidAccountingDocumentExistException
     * @throws com.ets.exception.PastSegmentException
     */
    //@Transactional
    public synchronized String delete(Long id, Date todayFromUsersMachine) throws ValidAccountingDocumentExistException, PastSegmentException, ValidTicketExist {
        Pnr pnr = dao.getByIdWithChildren(id);

        if (pnrDeleteLogic.deletable(pnr, todayFromUsersMachine)) {
            //Delete remakrs
            remarkService.deleteRemarks(pnr.getId());

            // Children Accounting documents prevent deleting Pnr. These needs to be deleted before deleting Pnr.   
            List<TicketingSalesAcDoc> accountingDocs = tSalesAcDocService.getByPnrId(pnr.getId());
            //Set<TicketingSalesAcDoc> valid_accountingDocs = AcDocUtil.filterVoidDocuments(new HashSet(accountingDocs));

            Set<TicketingSalesAcDoc> void_accountingDocs = new HashSet(AcDocUtil.getVoidSalesDocuments(new ArrayList(accountingDocs)));

            if (!void_accountingDocs.isEmpty()) {
                tSalesAcDocService.deleteBulk(void_accountingDocs);
            }

            Set<TicketingPurchaseAcDoc> p_docs = new HashSet(tPurchaseAcDocService.getByPnrId(pnr.getId()));

            if (!p_docs.isEmpty()) {
                tPurchaseAcDocService.deleteBulk(p_docs);
            }

            dao.delete(pnr);
            return "Deleted";
        }
        return "";
    }

    public List<Pnr> getByGDSPnr(String gdsPnr) {
        List<Pnr> list = new ArrayList<>();
        list = dao.getByGDSPnr(gdsPnr);
        for (Pnr p : list) {
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return list;
    }

    public List<Pnr> getByTktNo(String ticketNo) {
        List<Pnr> list = new ArrayList<>();
        list = dao.getByTktNo(ticketNo);
        for (Pnr p : list) {
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return list;
    }

    public List<Pnr> getByInvRef(String invref) {
        List<Pnr> list = new ArrayList<>();
        list = dao.getByInvRef(invref);
        for (Pnr p : list) {
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return list;
    }

    public List<Pnr> getPnrByName(String surName, String foreName) {
        List<Pnr> list = new ArrayList<>();
        list = dao.searchByPaxName(surName, foreName);
        for (Pnr p : list) {
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return list;
    }

    public Pnr getByIdWithChildren(long id) {
        Pnr pnr = dao.getByIdWithChildren(id);
        if (pnr != null) {
            PnrUtil.undefinePnrChildren(pnr);
        }
        return pnr;
    }

    public ATOLCertificate getAtolCertificate(long pnrid, Date issueDate) {
        Pnr pnr = getByIdWithChildren(pnrid);
        MainAgent mainAgent = AppSettingsService.mainAgent;
        ATOLCertificate certificate
                = ATOLCertificate.serializeToCertificate(DateUtil.dateToString(issueDate, "dd/MM/yyyy"),
                        mainAgent, pnr);
        return certificate;
    }

    public Pnrs pnrHistory(String issueDateFrom, String issueDateTo, String ticketingAgtOid, String bookingAgtOid) {

        if ("null".equals(ticketingAgtOid)) {
            ticketingAgtOid = null;
        }

        if ("null".equals(bookingAgtOid)) {
            bookingAgtOid = null;
        }

        Date dateFrom = DateUtil.stringToDate(issueDateFrom, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(issueDateTo, "ddMMMyyyy");

        String[] tktedOIDs = null;
        String[] bokingOIDs = null;

        if ("null".equals(ticketingAgtOid)) {
            ticketingAgtOid = null;
        } else if (ticketingAgtOid != null) {
            tktedOIDs = ticketingAgtOid.split(",");
        }

        if ("null".equals(bookingAgtOid)) {
            bookingAgtOid = null;
        } else if (bookingAgtOid != null) {
            bokingOIDs = bookingAgtOid.split(",");
        }

        List<Pnr> pnrList = dao.find(dateFrom, dateTo, tktedOIDs, bokingOIDs);
        for (Pnr p : pnrList) {
            PnrUtil.undefinePnrChildren(p);
        }

        Pnrs pnrs = new Pnrs();
        pnrs.setList(pnrList);

        return pnrs;
    }

    public List<Pnr> searchUninvoicedPnr() {
        List<Pnr> pnrList = dao.searchUninvoicedPnr();
        for (Pnr p : pnrList) {
            p.setSegments(null);
            //p.setRemarks(null);
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return pnrList;
    }

    public List<Pnr> searchPnrsToday(String dateString) {
        Date date = DateUtil.stringToDate(dateString, "ddMMMyyyy");
        List<Pnr> pnrList = dao.searchPnrsToday(date);
        for (Pnr p : pnrList) {
            PnrUtil.undefinePnrInTickets(p, p.getTickets());
        }
        return pnrList;
    }
}
