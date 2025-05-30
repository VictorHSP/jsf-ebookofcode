package br.com.ebookofcode.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_role")
public class Role extends BaseEntity<Long> {

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<User> users = new HashSet<>();

  public Role() {}

  public Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String role) {
    this.name = role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
