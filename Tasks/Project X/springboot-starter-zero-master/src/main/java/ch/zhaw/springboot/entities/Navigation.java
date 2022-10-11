package ch.zhaw.springboot.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity(name = "Navigation")
@Table(name = "navigation")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="navigation_type", discriminatorType = DiscriminatorType.STRING)
public class Navigation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_navigation_id")
	private long id;
	
	@Column(name = "layout", length = 50)
	private String layout;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "fk_navigation_id")
	private List<Provision> provisions;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_id")
	private Menu menu;	//parent
	
	public Navigation() {
		this.provisions = new ArrayList<Provision>();
	}

	public Navigation(String layout) {
		this.layout = layout;
		this.provisions = new ArrayList<Provision>();
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public long getId() {
		return id;
	}

	public List<Provision> getProvisions() {
		return provisions;
	}

	public void setProvisions(List<Provision> provisions) {
		this.provisions = provisions;
	}
	
	@JsonIgnore
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
		menu.addChild(this);
	}

}
