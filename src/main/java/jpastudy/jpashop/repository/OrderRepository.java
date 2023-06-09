package jpastudy.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.domain.item.QItem;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpastudy.jpashop.domain.dto.OrderSearch;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	//Order => Member, Delivery
	public List<Order> findAllWithMemberDelivery() {
		QOrder order = QOrder.order;
		QMember member = QMember.member;
		QDelivery delivery = QDelivery.delivery;

		return queryFactory
				.select(order)
				.from(order)
				.join(order.member, member)
				.fetchJoin()
				.join(order.delivery, delivery)
				.fetchJoin()
				.fetch();

//		return em.createQuery(
//						"select o from Order o" +
//								" join fetch o.member m" +
//								" join fetch o.delivery d", Order.class)
//				.getResultList();
	}
	//Order => Member, Delivery, OrderItems, Item
	//HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
	public List<Order> findAllWithItem() {
//		QOrder order = QOrder.order;
//		QMember member = QMember.member;
//		QDelivery delivery = QDelivery.delivery;
//		QOrderItem orderItem = QOrderItem.orderItem;
//		QItem item = QItem.item;
//
//		return queryFactory
//				.selectFrom(order)
//				.join(order.member, member).fetchJoin()
//				.join(order.delivery, delivery).fetchJoin()
//				.leftJoin(order.orderItems, orderItem)
//				.leftJoin(orderItem.item, item).fetchJoin()
//				.distinct()
//				.offset(0)
//				.limit(100)
//				.fetch();

		return em.createQuery(
						"select distinct o from Order o" +
								" join fetch o.member m" +
								" join fetch o.delivery d" +
								" join fetch o.orderItems oi" +
								" join fetch oi.item i", Order.class)
				.setFirstResult(0)
				.setMaxResults(100)
				.getResultList();
	}

	public List<Order> findAllWithMemberDelivery(int offset, int limit) {
//		return em.createQuery(
//						"select o from Order o" +
//								" join fetch o.member m" +
//								" join fetch o.delivery d", Order.class)
//				.setFirstResult(offset)
//				.setMaxResults(limit)
//				.getResultList();
		QOrder order = QOrder.order;
		QMember member = QMember.member;
		QDelivery delivery = QDelivery.delivery;

		return queryFactory
				.selectFrom(order)
				.join(order.member, member)
				.fetchJoin()
				.join(order.delivery, delivery)
				.fetchJoin()
				.offset(offset)
				.limit(limit)
				.fetch();
	}

	public List<Order> findAll(OrderSearch orderSearch) {
		QOrder order = QOrder.order;
		QMember member = QMember.member;

		List<Order> orderList = queryFactory
				.select(order)
				.from(order)
				.join(order.member, member)
//				.where(order.status.eq(orderSearch.getOrderStatus()),
//						member.name.like(orderSearch.getMemberName()))
				.where(statusEq(orderSearch.getOrderStatus()),
						nameLike(orderSearch.getMemberName()))
				.limit(1000)
				.fetch();
		return orderList;
	}

	private BooleanExpression statusEq(OrderStatus statusCond) {
		if (statusCond == null) {
			return null;
		}
		return QOrder.order.status.eq(statusCond);
	}
	private BooleanExpression nameLike(String nameCond) {
		if (!StringUtils.hasText(nameCond)) {
			return null;
		}
		return QMember.member.name.like(nameCond);
	}

	public List<Order> findAllJPQL(OrderSearch orderSearch) {
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
		// 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();

	}
}
