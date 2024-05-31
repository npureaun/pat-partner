package team.springpsring.petpartner.domain.feed.entity

import jakarta.persistence.*
import team.springpsring.petpartner.domain.feed.comment.entity.Comment
import team.springpsring.petpartner.domain.feed.comment.entity.toResponse
import team.springpsring.petpartner.domain.feed.dto.FeedResponse
import java.time.LocalDateTime

@Entity
@Table(name = "feed")
class Feed(

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "body", nullable = false)
    var body: String,

    @Column(name = "images", nullable = false)
    var images: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    var category: CategoryType,

    @Column(name = "views", nullable = false)
    var views: Int=0,

    @Column(name = "created_at", nullable = false)
    var created: LocalDateTime=LocalDateTime.now(),

    @OneToMany(mappedBy = "feed", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var comments: MutableList<Comment> = mutableListOf(),

//    @OneToMany(mappedBy = "feed",cascade = [(CascadeType.ALL)], orphanRemoval = true)
//    var loves : MutableList<Love> = mutableListOf(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun addComment(comment:Comment){
        comments.add(comment)
    }

    fun deleteComment(comment: Comment){
        comments.remove(comment)
    }
}

fun Feed.toResponse(loveCnt: Int=0): FeedResponse {
    return FeedResponse(
        id = id!!,
        name = name,
        title = title,
        body = body,
        images = images,
        category = category,
        views = views,
        created = created,
        loveCnt = loveCnt,
        comments =comments.map{it.toResponse()}
    )
}