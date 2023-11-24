package by.temniakov.testtask.store.entities;

import by.temniakov.testtask.enums.RegexpConstants;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.validation.annotation.NullOrNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@BatchSize(size = 25)
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Address.class)
    @JoinColumn(name = "id_address", referencedColumnName = "id")
    private Address address;

    @Column
    private String username;

    @Column(name = "order_time",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant orderTime = Instant.now();

    @Column(name = "phone_number")
    private String phoneNumber;

    @NullOrNotBlank
    @Pattern(message = "Email is not valid",
            regexp = RegexpConstants.EMAIL)
    @Column(name = "user_email")
    private String userEmail;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.valueOf("DRAFT");

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE},
            targetEntity = GoodOrder.class, fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private List<GoodOrder> goodAssoc = new ArrayList<>();

    private static Instant $default$orderTime() {
        return Instant.now();
    }

    private static Status $default$status() {
        return Status.valueOf("DRAFT");
    }

    private static List<GoodOrder> $default$goodAssoc() {
        return new ArrayList<>();
    }

    public static OrdersBuilder builder() {
        return new OrdersBuilder();
    }

    public static class OrdersBuilder {
        private Integer id;
        private Address address;
        private String username;
        private Instant orderTime$value;
        private boolean orderTime$set;
        private String phoneNumber;
        private @Pattern(message = "Email is not valid",
                regexp = RegexpConstants.EMAIL) String userEmail;
        private Status status$value;
        private boolean status$set;
        private List<GoodOrder> goodAssoc$value;
        private boolean goodAssoc$set;

        OrdersBuilder() {
        }

        public OrdersBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public OrdersBuilder address(Address address) {
            this.address = address;
            return this;
        }

        public OrdersBuilder username(String username) {
            this.username = username;
            return this;
        }

        public OrdersBuilder orderTime(Instant orderTime) {
            this.orderTime$value = orderTime;
            this.orderTime$set = true;
            return this;
        }

        public OrdersBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public OrdersBuilder userEmail(@Pattern(message = "Email is not valid",
                regexp = RegexpConstants.EMAIL) String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public OrdersBuilder status(Status status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        public OrdersBuilder goodAssoc(List<GoodOrder> goodAssoc) {
            this.goodAssoc$value = goodAssoc;
            this.goodAssoc$set = true;
            return this;
        }

        public Orders build() {
            Instant orderTime$value = this.orderTime$value;
            if (!this.orderTime$set) {
                orderTime$value = Orders.$default$orderTime();
            }
            Status status$value = this.status$value;
            if (!this.status$set) {
                status$value = Orders.$default$status();
            }
            List<GoodOrder> goodAssoc$value = this.goodAssoc$value;
            if (!this.goodAssoc$set) {
                goodAssoc$value = Orders.$default$goodAssoc();
            }
            return new Orders(this.id, this.address, this.username, orderTime$value, this.phoneNumber, this.userEmail, status$value, goodAssoc$value);
        }

        public String toString() {
            return "Orders.OrdersBuilder(id=" + this.id + ", address=" + this.address + ", username=" + this.username + ", orderTime$value=" + this.orderTime$value + ", phoneNumber=" + this.phoneNumber + ", userEmail=" + this.userEmail + ", status$value=" + this.status$value + ", goodAssoc$value=" + this.goodAssoc$value + ")";
        }
    }
}

