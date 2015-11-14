package com.pwc.hibernate.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pwc.hibernate.Pic;

@Component
public class PicDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List<Pic> findAll() {
		Session session = null;
		List<Pic> pics = null;
		try {
			session = sessionFactory.openSession();
			pics = session.createQuery("from com.pwc.hibernate.Pic").list();
			// System.out.println(pics.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session.isOpen()){
				session.close();
			}
		}
		// Collections.shuffle(pics);

		return pics;
	}

	public List<Pic> findAllSorted() {
		Session session = null;
		List<Pic> pics = null;
		try {
			session = sessionFactory.openSession();

			pics = session.createQuery("from com.pwc.hibernate.Pic p where p.picCount != 0 order by p.picCount desc")
					.list();

			// System.out.println(pics.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session.isOpen()){
				session.close();
			}
		}
		return pics;
	}

	public Pic findById(int id) {
		Session session = null;
		Pic pic = null;
		try {
			session = sessionFactory.openSession();
			pic = (Pic) session.get(Pic.class, id);
			// System.out.println(pics.size());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session.isOpen()){
				session.close();
			}
		}
		return pic;

	}

	public Pic ninki(int id) {
		Session session = null;
		Pic pic = null;
		try {
			pic = this.findById(id);
			String tmp5 = pic.getTmp5(); // ninki
			int ninki = 0;
			if (tmp5 != null) {
				ninki = Integer.valueOf(tmp5);
			}
			ninki += 1;
			pic.setTmp5(String.valueOf(ninki));
			this.update(pic);

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(session.isOpen()){
				session.close();
			}
		}
		return pic;

	}

	public void update(Pic pic) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			if (session == null){
				session = sessionFactory.openSession();
			}
			
			Transaction tx = session.beginTransaction();
			Pic vpic = (Pic) this.findById(pic.getIdpic());
			pic.setPicCount(pic.getPicCount() + 1);
			session.update(pic);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void add(Pic pic) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			if (session == null){
				session = sessionFactory.openSession();
			}
			Transaction tx = session.beginTransaction();
			pic.setPicCount(0);
			session.save(pic);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
