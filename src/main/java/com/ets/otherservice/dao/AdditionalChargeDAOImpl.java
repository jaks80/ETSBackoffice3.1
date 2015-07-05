package com.ets.otherservice.dao;

import com.ets.GenericDAOImpl;
import com.ets.otherservice.domain.AdditionalCharge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("additionalChargeDAO")
@Transactional
public class AdditionalChargeDAOImpl extends GenericDAOImpl<AdditionalCharge, Long> implements AdditionalChargeDAO{
    
}
