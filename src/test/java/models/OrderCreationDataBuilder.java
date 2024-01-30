import java.util.List;

public class OrderCreationDataBuilder {

    public final OrderCreationData orderCreationData;

    public OrderCreationDataBuilder() {
        this.orderCreationData = new OrderCreationData();
    }

    public OrderCreationDataBuilder withFirstName(String firstName) {
        orderCreationData.setFirstName(firstName);
        return this;
    }

    public OrderCreationDataBuilder withLastName(String lastName) {
        orderCreationData.setLastName(lastName);
        return this;
    }

    public OrderCreationDataBuilder withAddress(String address) {
        orderCreationData.setAddress(address);
        return this;
    }

    public OrderCreationDataBuilder withMetroStation(int metroStation) {
        orderCreationData.setMetroStation(metroStation);
        return this;
    }

    public OrderCreationDataBuilder withPhone(String phone) {
        orderCreationData.setPhone(phone);
        return this;
    }

    public OrderCreationDataBuilder withRentTime(int rentTime) {
        orderCreationData.setRentTime(rentTime);
        return this;
    }

    public OrderCreationDataBuilder withDeliveryDate(String deliveryDate) {
        orderCreationData.setDeliveryDate(deliveryDate);
        return this;
    }

    public OrderCreationDataBuilder withComment(String comment) {
        orderCreationData.setComment(comment);
        return this;
    }

    public OrderCreationDataBuilder withColor(List<String> color) {
        orderCreationData.setColor(color);
        return this;
    }

    public OrderCreationDataBuilder withStatusCode(int statusCode) {
        orderCreationData.setStatusCode(statusCode);
        return this;
    }

    public OrderCreationData build() {
        return orderCreationData;
    }
}
