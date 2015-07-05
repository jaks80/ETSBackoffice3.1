
package com.ets.accountingdoc.model;

import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.util.DateUtil;
import com.ets.util.Enums;

/**
 *
 * @author Yusuf
 */
public class RelatedDocSummery {
    
    private String date;
    private String type;
    private String remark;
    private String amount;

    public static RelatedDocSummery documentToSummery(TicketingSalesAcDoc d){
     RelatedDocSummery sm = new RelatedDocSummery();
     sm.setDate(DateUtil.dateToString(d.getDocIssueDate()));
     sm.setType(d.getType().toString());
     sm.setAmount(d.getDocumentedAmount().abs().toString());
     
     StringBuilder remark = new StringBuilder();
     if(d.getRemark()!=null){
      remark.append(d.getRemark()).append(" ");
     }
     if(d.getType().equals(Enums.AcDocType.PAYMENT) || d.getType().equals(Enums.AcDocType.REFUND)){
      remark.append(d.getPayment().getRemark()).append("/").append(d.getPayment().getPaymentType().toString());
     }
     
     String _remark = remark.toString();
     sm.setRemark(_remark);
     return sm;
    }
    
    public static RelatedDocSummery documentToSummery(OtherSalesAcDoc d){
     RelatedDocSummery sm = new RelatedDocSummery();
     sm.setDate(DateUtil.dateToString(d.getDocIssueDate()));
     sm.setType(d.getType().toString());
     sm.setAmount(d.getDocumentedAmount().toString());
     
     StringBuilder remark = new StringBuilder();
     if(d.getRemark()!=null){
      remark.append(d.getRemark()).append(" ");
     }
     if(d.getType().equals(Enums.AcDocType.PAYMENT) || d.getType().equals(Enums.AcDocType.REFUND)){
      remark.append(d.getPayment().getRemark()).append("/").append(d.getPayment().getPaymentType().toString());
     }
     
     String _remark = remark.toString();
     sm.setRemark(_remark);
     return sm;
    }
       
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
}
