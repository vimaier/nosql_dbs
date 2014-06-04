package de.graphdb.form;

import java.util.Collection;

import de.graphdb.dto.UserDTO;

public class SearchForm {

  private String search;
  private Collection<UserDTO> result;

  public SearchForm(String search, Collection<UserDTO> result) {
    super();
    this.search = search;
    this.result = result;
  }

  public SearchForm() {
    super();
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public Collection<UserDTO> getResult() {
    return result;
  }

  public void setResult(Collection<UserDTO> result) {
    this.result = result;
  }

}
