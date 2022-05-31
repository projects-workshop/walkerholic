import React from 'react'
import UserCard from '../UserCard'

function FollowModal({follows, isFollowers, isFollowings, setIsFollowers, setIsFollowings}) {

    const handleClose = () =>{
        if(isFollowers){
            setIsFollowers(false)
        }else{
            setIsFollowings(false)
        }
    }
    return (
        <div className="follow">
            <div className="follow_box">
                <div className="follow_content">
                    <div className="follow_title">
                        {isFollowers ? 'Followers' : 'Followings'}
                        <hr/>
                    </div>
                    {
                        follows && follows.map((follow, index)=>(
                            <UserCard key={index} user={follow.user} isFollowers={isFollowers} isFollow={true} setIsFollowers={setIsFollowers} setIsFollowings={setIsFollowings}/> 
                        ))
                    }
                </div>

                <div className="form_button">
                    <button onClick={handleClose}>Close</button>
                </div>
            </div>
        </div>
    )
}

export default FollowModal
