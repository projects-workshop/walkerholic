import React from 'react'
import basicProfile from '../../images/basicProfile.svg'
import ProgressiveImage from 'react-progressive-graceful-image'


function HomePostCard({post}) {
    return (
        <div className="home_post">
            <div className="home_postcard_image">
                <ProgressiveImage src={post.imageUrl} >
                    {(src) => <img src={src} alt="" />}
                </ProgressiveImage>
                {/* <img src={post.imageUrl} alt="" /> */}
            </div>
            <div className="home_postcard_title">
                {post.title}
            </div>
            <div className="home_postcard_user">
                <div className="home_postcard_user_image">
                    <ProgressiveImage src={post.userImageUrl? post.userImageUrl : basicProfile} >
                        {(src) => <img src={src} alt="userImage" />}
                    </ProgressiveImage>
                    {/* <img src={post.userImageUrl? post.userImageUrl : basicProfile} alt="userImage" /> */}                </div>
                <div>{post.userName}</div>
            </div>
        </div>
    )
}

export default HomePostCard
