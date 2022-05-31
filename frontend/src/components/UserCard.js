import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import basicProfile from '../images/basicProfile.svg'
import { follow, unfollow } from '../_actions/FollowActions'
import { useHistory } from "react-router-dom";


function UserCard({user,isFollow, isFollowers, setIsFollowers, setIsFollowings}) {
    
    const auth = useSelector(state => state.auth)

    const dispatch = useDispatch()
    const history = useHistory()

    const handleFollow = () =>{
        dispatch(follow(user.id))
    }
    const handleUnfollow = () =>{
        const follow = auth.user.followings.filter(follow=>follow.user.id ===user.id)
        dispatch(unfollow(follow[0].id))
    }

    const handlePage = () =>{
        history.push(`/user/${user.id}`)
        if(isFollowers){
            setIsFollowers(false)
        }else {
            setIsFollowings(false)
        }
    }

    return (
        <div className="user_card">
            <div onClick={handlePage} style={{cursor:'pointer'}}>
                <img src={user.imageUrl ? user.imageUrl : basicProfile} alt="profileImage" />
                <span>{user.fullname}</span>
            </div>

            {
                user.id !== auth.user.id && 
                (
                    isFollow&&
                    auth.user.followings.filter(follow=>follow.user.id ===user.id).length>0
                    ? <button className="unfollow_button" onClick={handleUnfollow}>Unfollow</button>
                    : <button className="follow_button" onClick={handleFollow}>Follow</button>
                )
            }
        </div>
    )
}

export default UserCard
