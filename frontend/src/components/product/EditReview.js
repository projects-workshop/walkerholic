import React from 'react'
import { useState } from 'react'
import { useDispatch } from 'react-redux'
import { editReview } from '../../_actions/ReviewActions'

function EditReview({review, setIsEdit}) {

    const [rating, setRating] = useState(review.rating)
    const [comment, setComment] = useState(review.comment)

    const ratingNumbers = Array(1*5).fill().map((arr,i)=>{return i})

    const dispatch = useDispatch()

    const handleEditReview = (e) =>{
        e.preventDefault()

        const reviewRequest = {
            rating:rating,
            comment:comment
        }

        dispatch(editReview(reviewRequest, review.id)).then(res=>{
            setIsEdit(false)
        })
    }

    return (
        <div className="edit_profile ">
                <form onSubmit={handleEditReview}>

                    <div className="auth_message">
                        Edit Review
                    </div>

                    <div className="edit_review_rating">
                        <i className="fab fa-creative-commons-zero" onClick={()=>setRating(0)}></i>
                        {
                            ratingNumbers.map((r,index)=>(
                                <i className={review.rating>=(r+1)? "fas fa-thumbs-up":"far fa-thumbs-up"} onClick={()=>setRating(r+1)} key={index}></i>
                            ))
                        }
                    </div>

                    <div className="edit_review_comment">
                        <textarea name="comment" id="comment" cols="30" rows="5" value={comment} onChange={(e)=>setComment(e.target.value)}></textarea>
                    </div>


                    <div className="form_button">
                    <button className="follow_button" style={{marginRight:"1rem"}} type="submit">Save</button>
                    <button className="unfollow_button" onClick={()=>setIsEdit(false)}>Cancel</button>
                    </div>
                </form>
            </div>
    )
}

export default EditReview
