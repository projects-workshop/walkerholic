import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import EditPost from './EditPost'
import {Link} from 'react-router-dom'
import moment from 'moment'
import basicProfile from '../../images/basicProfile.svg'
import { deletePost } from '../../_actions/PostActions'


function CardHeader({post}) {

    const auth = useSelector(state => state.auth)

    const [isEdit, setIsEdit] = useState(false)

    const dispatch = useDispatch()

    const handleDeletePost = () =>{
        if(window.confirm('Are you sure to delete this post?')){
            dispatch(deletePost(post.id))
        }
    }
    return (
        <div className="postcard_header">
            <div className="postcard_title">
                {post.title}
            </div>

            <div>
                {
                    auth.user.id === post.user.id &&
                    <div className="nav-item dropdown" style={{display:"flex",justifyContent:"flex-end"}}>
                        <span className="material-icons" id="moreLink" data-toggle="dropdown" style={{cursor:"pointer"}}>
                            more_horiz
                        </span>
                        <div className="dropdown-menu">
                            <div className="dropdown-item" onClick={()=>setIsEdit(!isEdit)}>
                                Edit Post
                            </div>
                            <div className="dropdown-item" onClick={handleDeletePost}>
                                Delete Post
                            </div>
                        </div>
                    </div>
                }
                <div className="postcard_user_info">
                    <div>
                        <img src={post.user.imageUrl? post.user.imageUrl : basicProfile} alt="userImage" />
                    </div>
                    <div className="postcard_user_name">
                        <div style={{fontSize:'14px', fontWeight:"600"}}> 
                            <Link to={`/user/${post.user.id}`}>{post.user.fullname}</Link>
                        </div>
                        <div>
                            {moment(post.createdAt).format('YYYY MM DD HH:mm:ss')}
                        </div>
                    </div>
                </div>
            </div>

            {
                isEdit &&
                <EditPost setIsEdit={setIsEdit} post={post}/>
            }
        </div>
    )
}

export default CardHeader
