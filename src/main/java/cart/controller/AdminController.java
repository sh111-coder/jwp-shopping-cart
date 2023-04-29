package cart.controller;

import cart.controller.dto.ProductModifyRequest;
import cart.service.CartService;
import cart.controller.dto.ProductRegisterRequest;
import cart.service.dto.ProductResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CartService cartService;

    public AdminController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showAllProducts(Model model) {
        List<ProductResponse> allProducts = cartService.findAllProducts();
        model.addAttribute("products", allProducts);
        return "admin";
    }

    @PostMapping("/product")
    public ResponseEntity<Void> registerProduct(@RequestBody @Valid ProductRegisterRequest productRegisterRequest) {
        long savedId = cartService.save(productRegisterRequest);
        return ResponseEntity.created(URI.create("/admin/product/" + savedId)).build();
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Void> modifyProduct(@RequestBody @Valid ProductModifyRequest productModifyRequest,
                                              @PathVariable long id) {
        cartService.modifyById(productModifyRequest, id);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable long id) {
        cartService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}