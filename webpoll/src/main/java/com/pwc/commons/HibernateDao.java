package com.pwc.commons;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.pwc.hibernate.Pic;

public class HibernateDao {
	private static SessionFactory sessionFactory;

	public HibernateDao() {
		try {
			// 读取hibernate.cfg.xml文件
			Configuration cfg = new Configuration().configure("com/pwc/utils/hibernate.cfg.xml");
			// 建立SessionFactory
			sessionFactory = cfg.buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(Object object) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Pic pic = (Pic) object;
		pic.setPicCount(0);
		session.save(pic);
		session.close();
		tx.commit();
	}

	public static void main(String[] args) {
		HibernateDao dao = new HibernateDao();
		Pic pic = new Pic();
		for (int i = 0; i < 100; i++) {
			dao.save(pic);
		}
	}
}
