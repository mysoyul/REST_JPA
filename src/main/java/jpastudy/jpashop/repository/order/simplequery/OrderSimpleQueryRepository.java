package jpastudy.jpashop.repository.order.simplequery;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpastudy.jpashop.domain.QDelivery;
import jpastudy.jpashop.domain.QMember;
import jpastudy.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryDto" +
                                "(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtosDSL() {
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QDelivery delivery = QDelivery.delivery;

        List<OrderSimpleQueryDto> orderSimpleQueryDtoList = jpaQueryFactory
                .select(Projections.constructor(OrderSimpleQueryDto.class,
                        order.id, member.name, order.orderDate, order.status, delivery.address))
                .from(order)
                .join(order.member, member)
                .join(order.delivery, delivery)
                .fetch();
        return orderSimpleQueryDtoList;
    }


}
