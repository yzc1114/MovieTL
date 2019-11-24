import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String price;
    private String format;

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", price='" + price + '\'' +
                ", format='" + format + '\'' +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
