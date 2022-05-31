import React from 'react'
import { useSelector } from 'react-redux'
import { Link,useHistory } from 'react-router-dom'
import earth from '../images/earth.svg'

function PostThumb({posts}) {
    const auth = useSelector(state => state.auth)

    const history = useHistory()

    const handleOnclick = (postId) =>{
        if(!auth.user){
            history.push('/signin')
        }else{
            history.push(`/post/${postId}`)
        }
    }
    return (
        <div className="post_thumb_container">
            {
                
                posts.map((post, index)=>(
                    <div className="post_thumb" key={index}>
                        <div className="post_thumb_image" onClick={()=>handleOnclick(post.id)}>
                            <img src={post.imageUrl} alt="postImage" />
                        </div>
                        <div className="post_thumb_title" onClick={()=>handleOnclick(post.id)}>{post.title}</div>
                        <div className="post_thumb_user">
                            <div className="post_thumb_user_image">
                                <img src={post.userImageUrl ? post.userImageUrl : earth} alt="" />
                            </div>
                            <div style={{marginLeft:"7px"}}>{post.userName}</div>
                        </div>
                    </div>
                ))
            }
        </div>
    )
}

export default PostThumb
