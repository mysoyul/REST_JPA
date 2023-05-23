package jpastudy.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member; // 주문 회원

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
			fetch = FetchType.LAZY )
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery; // 배송정보

	private LocalDateTime orderDate; // 주문시간

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // 주문상태 [ORDER, CANCEL]

	// ==연관관계 메서드==//
	public void setMember(Member member) {

	}

	public void addOrderItem(OrderItem orderItem) {

	}

	public void setDelivery(Delivery delivery) {

	}

	// == 비즈니스 로직 : 주문생성 메서드==//
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();

		return order;
	}

	// ==비즈니스 로직 : 주문 취소 ==//
	public void cancel() {

	}

	// ==비즈니스 로직 : 전체 주문 가격 조회 ==//
	public int getTotalPrice() {
		int totalPrice = 0;

		return totalPrice;
	}

}
