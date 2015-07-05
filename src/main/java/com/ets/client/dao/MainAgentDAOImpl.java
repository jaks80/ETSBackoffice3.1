package com.ets.client.dao;

import com.ets.GenericDAOImpl;
import com.ets.client.domain.MainAgent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("mainAgentDAO")
@Transactional
public class MainAgentDAOImpl extends GenericDAOImpl<MainAgent, Long> implements MainAgentDAO{    
           
}
