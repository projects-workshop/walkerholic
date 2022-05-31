import React from 'react'
import moment from 'moment'
import {Link} from 'react-router-dom'
import basicProfile from '../../images/basicProfile.svg'
import LikeButton from '../posts/LikeButton'

function HomePostCard({post}) {
    return (
        <div className="home_post">
            <div className="home_postcard_image">
                <img src={post.imageUrl} alt="" />
            </div>
            <div className="home_postcard_title">
                {post.title}
            </div>
            <div className="home_postcard_user">
                <div className="home_postcard_user_image">
                    <img src={post.userImageUrl? post.userImageUrl : basicProfile} alt="userImage" />
                </div>
                <div>{post.userName}</div>
            </div>
        </div>
    )
}

export default HomePostCard
