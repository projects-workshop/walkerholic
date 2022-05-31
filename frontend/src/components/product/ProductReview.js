import React from 'react'
import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { createReview } from '../../_actions/ReviewActions'
import ReviewCard from './ReviewCard'
import Review from '../../images/Review.svg'

function ProductReview({reviews, productId}) {

    const [rating, setRating] = useState(0)
    const [comment, setComment] = useState('')

    const ratingNumbers = Array(1*5).fill().map((arr,i)=>{return i})

    const dispatch = useDispatch()

    const handleCreateReview = (e) =>{
        e.preventDefault()

        dispatch(createReview(rating,comment,productId)).then(res=>{
            setRating(0)
            setComment('')
        })
    }
    return (
        <>
            <div className="product_reviews">
                <div className="product_reviews_title">
                    <img src={Review} alt="" />
                </div>
                <div className="product_review">
                {
                    reviews.length===0
                    ? <div className="product_reviews_title" style={{textAlign:"center", transform:"translateY(11rem)", color:"white", WebkitTextStroke:"2px black"}}>No reviews yet.</div> 
                    : <div >
                        {
                            reviews.map((review,index)=>(
                                <ReviewCard review={review} key={index}/>
                            ))
                        }
                    </div>
                }
                </div>
            </div>
            <div className="product_write_review">
                <form onSubmit={handleCreateReview}>

                    <div className="product_write_review_title">
                        How was it ? 
                    </div>

                    <div>
                        <i className="fab fa-creative-commons-zero" onClick={()=>setRating(0)}></i>
                        {
                            ratingNumbers.map((r,index)=>(
                                <i className={rating>=(r+1)? "fas fa-thumbs-up":"far fa-thumbs-up"} onClick={()=>setRating(r+1)} key={index}></i>
                            ))
                        }
                    </div>

                    <div>
                        <textarea name="comment" id="comment" cols="30" rows="5" value={comment} onChange={(e)=>setComment(e.target.value)}></textarea>
                    </div>


                    <div className="form_button">
                        <button>Save</button>
                    </div>
                </form>
            </div>
        </>
    )
}

export default ProductReview
