package com.pwc.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pwc.hibernate.IpPool;

@Component
public class IPPoolDao {
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * if true then have duplicated ip in pool if false then save it to ip pool
	 * (no duplicated)
	 * 
	 * @param ip
	 * @param picId
	 * @return
	 */
	public boolean putPool(String ip, int picId) {
		boolean returnValue = true;
		Session session = sessionFactory.openSession();
		session.getTransaction().begin();
		IpPool pool = new IpPool();
		pool.setIpaddress(ip);
		pool.setVote4picid(picId);
		// if (!isExistedInPool(ip)) {
		session.save(pool);
		// returnValue = false;
		// } else {
		// returnValue = true;
		// }
		session.getTransaction().commit();

		if (session.isOpen()) {
			session.close();
		}

		return returnValue;
	}

	/**
	 * if true then existed in pool if false then not existed
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isExistedInPool(String ip) {
		boolean returnValue = false;
		Session session = sessionFactory.openSession();
		try {
			Query query = session.createQuery("from com.pwc.hibernate.IpPool ip where ip.ipaddress =:ip");
			query.setString("ip", ip);
			List list = query.list();

			Iterator it = list.iterator();
			while (it.hasNext()) {
				IpPool ipp = (IpPool) it.next();
				System.out.print(ipp.getIpaddress() + "||");
			}
			System.out.println("---");
			if (list != null && list.size() != 0) {
				returnValue = true;
			} else {
				returnValue = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return returnValue;
	}
}
