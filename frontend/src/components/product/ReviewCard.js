import React from 'react'
import basicProfile from '../../images/basicProfile.svg'
import moment from 'moment'
import { useDispatch, useSelector } from 'react-redux'
import Rating from './Rating'
import { useState } from 'react'
import EditReview from './EditReview'
import { deleteReview } from '../../_actions/ReviewActions'

function ReviewCard({review}) {

    const auth = useSelector(state => state.auth)

    const [isEdit, setIsEdit] = useState(false)

    const dispatch = useDispatch()

    const handleDelete = () =>{
        if(window.confirm('Are you sure to delete this review?')){
            dispatch(deleteReview(review.id))
        }
    }
    return (
        <div className="review">
            <div className="review_card_header">
                <div className="review_user_info">
                    <div className="review_user_image">
                        <img src={review.userImageUrl? review.userImageUrl : basicProfile} alt="userImage" />
                    </div>
                    <div className="review_user_name">
                        <div style={{fontSize:"13px", fontWeight:"800"}}>
                            {review.userFullname}
                        </div>
                        <small>
                            {moment(review.createdAt).fromNow()}
                        </small>
                    </div>
                </div>

                {
                    auth.user && (auth.user.id === review.userId) &&
                    <div >
                        <i className="far fa-edit" onClick={()=>setIsEdit(true)}></i>
                        <i className="far fa-trash-alt" onClick={handleDelete}></i>
                    </div>
                }
            </div>

            <div className="review_card_body">
                <div className="review_rating">
                    <Rating rating={review.rating} />
                </div>

                <div className="review_comment">
                    <p>
                        {review.comment}
                    </p>
                </div>
            </div>

            {
                isEdit &&
                <EditReview review = {review} setIsEdit={setIsEdit}/>
            }
        </div>
    )
}

export default ReviewCard
