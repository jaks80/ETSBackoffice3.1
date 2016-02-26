package com.ets.pnr.dao;

import com.ets.GenericDAO;
import com.ets.pnr.domain.Remark;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface RemarkDAO extends GenericDAO<Remark, Long>{
    
    public int deleteRemarks(Long pnrId);
    
    public List<Remark> getByPnrId(Long pnr_id);
}
