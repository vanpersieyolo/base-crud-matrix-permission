// language: java
package org.example.basespringbootproject.web.controller;

import jakarta.validation.Valid;
import org.example.basespringbootproject.application.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic base controller exposing common CRUD + search endpoints.
 * Concrete controllers should extend and add class-level mapping and @RestController.
 * <p>
 * Example:
 *
 * @RestController
 * @RequestMapping("/users") public class UserController extends AbstractBaseController<UserDTO, Long> { ... }
 */
public abstract class AbstractBaseController<D, ID> {

    protected final IBaseService<D, ID> service;

    protected AbstractBaseController(IBaseService<D, ID> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody @Valid D dto) {
        D created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @RequestBody @Valid D dto) {
        D updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> findById(@PathVariable ID id) {
        Optional<D> opt = service.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<D>> findAll() {
        List<D> all = service.findAll();
        return ResponseEntity.ok(all);
    }

    /**
     * Search using query parameters as filters and Pageable for pagination.
     * Example request: GET /?username=alice&enabled=true&page=0&size=20
     */
    @GetMapping("/search")
    public ResponseEntity<Page<D>> search(@RequestParam Map<String, String> filters, Pageable pageable) {
        // convert String values to Object map (service will handle string comparisons)
        Map<String, Object> objectFilters = new HashMap<>(filters);
        Page<D> page = service.search(objectFilters, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable ID id) {
        boolean exists = service.exists(id);
        return ResponseEntity.ok(exists);
    }
}