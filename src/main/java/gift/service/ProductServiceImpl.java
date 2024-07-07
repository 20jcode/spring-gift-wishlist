package gift.service;

import gift.database.JdbcProductRepository;
import gift.dto.ProductDTO;
import gift.exceptionAdvisor.ProductServiceException;
import gift.model.Product;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final JdbcProductRepository jdbcProductRepository;

    public ProductServiceImpl(JdbcProductRepository jdbcProductRepository) {
        this.jdbcProductRepository = jdbcProductRepository;
    }

    @Override
    public List<ProductDTO> readAll() {
        var products = jdbcProductRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();

        for (var product : products) { //DTO로 전환
            productDTOList.add(new ProductDTO(product));
        }

        return productDTOList;
    }

    //새로운 상품 추가
    @Override
    public void create(ProductDTO prod) {
        checkKakao(prod.getName());
        jdbcProductRepository.create(
            new Product(null, prod.getName(), prod.getPrice(), prod.getImageUrl()));
    }


    @Override
    public void updateName(long id, String name) {
        var prod = jdbcProductRepository.findById(id);
        checkKakao(prod.getName());
        prod.setName(name);
        jdbcProductRepository.update(id, prod);

    }

    @Override
    public void updatePrice(long id, int price) {
        var prod = jdbcProductRepository.findById(id);
        prod.setPrice(price);
        jdbcProductRepository.update(id, prod);
    }

    @Override
    public void updateImageUrl(long id, String url) {
        var prod = jdbcProductRepository.findById(id);
        prod.setImageUrl(url);
        jdbcProductRepository.update(id, prod);
    }

    @Override
    public void delete(long id) {
        jdbcProductRepository.delete(id);
    }

    private void checkKakao(String productName) {
        if (productName.contains("카카오")) {
            throw new ProductServiceException("카카오 문구는 md협의 이후 사용할 수 있습니다.",
                HttpStatus.BAD_REQUEST);
        }
    }
}
