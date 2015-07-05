package com.ets.accountingdoc.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.AdditionalChargeLine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("additionalChgLineDAO")
@Transactional
public class AdditionalChgLineDAOImpl extends GenericDAOImpl<AdditionalChargeLine, Long> implements AdditionalChgLineDAO{
    
}
