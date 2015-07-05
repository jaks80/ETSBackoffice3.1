package com.ets.accountingdoc_o.dao;

import com.ets.GenericDAO;
import com.ets.accountingdoc.domain.AccountingDocumentLine;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface AccountingDocumentLineDAO extends GenericDAO<AccountingDocumentLine, Long>{
    
    public List<AccountingDocumentLine> findLineItems(Date from,Date to, Long categoryId, Long itemId,Enums.ClientType clienttype, Long clientid);
}
