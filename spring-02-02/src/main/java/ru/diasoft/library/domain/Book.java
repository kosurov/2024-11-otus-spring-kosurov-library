package ru.diasoft.library.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "author-genre-graph",
        attributeNodes = {
        @NamedAttributeNode("author"),
        @NamedAttributeNode("genre")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence_id")
    @SequenceGenerator(name = "book_sequence_id", sequenceName = "book_sequence_id", allocationSize = 1)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(name = "authorid")
    private Author author;

    @OneToOne
    @JoinColumn(name = "genreid")
    private Genre genre;

    @BatchSize(size = 5)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid")
    @ToString.Exclude
    private List<Comment> comments;
}
