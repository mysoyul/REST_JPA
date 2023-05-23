package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	private String name;

	private int price;

	private int stockQuantity;

	// ==비즈니스 로직==//
	//주문 취소할 때 호출
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	//주문 발생할 때 호출
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new NotEnoughStockException("재고 수량이 부족합니다.");
		}
		this.stockQuantity = restStock;
	}

}
