import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import EditProfile from '../components/profile/EditProfile'
import basicProfile from '../images/basicProfile.svg'
import { getProfile } from '../_actions/ProfileActions'
import {levelIconShow} from '../utils/MediaShow'
import PostThumb from '../components/PostThumb'
import FollowModal from '../components/profile/FollowModal'
import Seller from '../images/Seller.svg'
import { follow, unfollow } from '../_actions/FollowActions'
import Loading from '../components/Loading'
import Error from '../components/Error'

function ProfileScreen(props) {

    const id = props.match.params.id

    const [isEdit, setIsEdit] = useState(false)
    const [isFollowers, setIsFollowers] = useState(false)
    const [isFollowings, setIsFollowings] = useState(false)

    const [isLikePost, setIsLikePost] = useState(false)

    const dispatch = useDispatch()

    const auth = useSelector(state => state.auth)
    const profile = useSelector(state => state.profile)

    useEffect(() => {
        if (auth.user && id) {
            dispatch(getProfile(id))
        }
    }, [auth.user, id])

    const handleFollow = () =>{
        dispatch(follow(profile.user.id))
    }
    const handleUnfollow = () =>{
        const follow = profile.followers.filter(f=>f.user.id ===auth.user.id)
        dispatch(unfollow(follow[0].id))
    }

    return (
        <>
        {
            profile.error && profile.error.message && <Error error = {profile.error.message}/>
        }
        {
            profile.Loading && <Loading/>
        }

        {  (profile.loading ===false && profile.user.id) &&
        
            <div className="profile">
                <div className="profile_title">
                    Today's Earth saver
                </div>
                    <div>
                        <div className="profile_image" >
                            <img src={profile.user.imageUrl ? profile.user.imageUrl : basicProfile} alt="" />
                        </div>
                        <div className="profile_container">
                            <div className="profile_content_container">
                                <div className="profile_content" style={{fontSize:"1.4rem", fontWeight:"600"}}>
                                    {profile.user.firstname}&nbsp;{profile.user.lastname}
                                    {
                                        profile.user.role ==="SELLER" &&
                                        <img src={Seller} alt="role" className="level_icon"/>
                                    }
                                    {
                                        levelIconShow(profile.user.level)
                                    }
                                </div>
                                <div className="profile_content">
                                    {profile.user.email}
                                </div>
                                <div className="profile_content">
                                    {profile.user.phoneNumber}
                                </div>
                                <div className="profile_content" style={{fontSize:"13px", color:"gray"}}>
                                    {profile.user.description}
                                </div>
                                <div className="profile_content" style={{fontWeight:"800"}}>
                                    <span onClick={()=>setIsFollowers(true)}>{profile.followers.length} followers</span>
                                    &nbsp;&nbsp;|&nbsp;&nbsp; 
                                    <span onClick={()=>setIsFollowings(true)}>{profile.followings.length} followings</span>
                                </div>
                            </div>

                            <div className="profile_button_container">
                                {
                                    auth.user?.id !== profile.user?.id
                                    ?
                                        <div className="profile_follow_button">
                                            {
                                                profile.followers && profile.followers.filter(f=> f.user.id === auth.user.id).length >0
                                                ?<button className="unfollow_button" onClick={handleUnfollow}>Unfollow</button>
                                                :<button className="follow_button" onClick={handleFollow}>Follow</button>
                                            }
                                        </div>
                                    :
                                    <div className="profile_edit_button">
                                        <button onClick={()=>setIsEdit(!isEdit)} className="profile_button">Edit Profile</button>
                                    </div>
                                }
                            </div>
                        </div>
                        {
                            isEdit 
                            ? <EditProfile setIsEdit={setIsEdit}/>
                            : isFollowers
                                ? <FollowModal setIsFollowers={setIsFollowers} isFollowers={isFollowers} follows={profile.followers}/>
                                : isFollowings && <FollowModal setIsFollowings={setIsFollowings} isFollowings={isFollowings} follows={profile.followings}/>
                        }
                    </div>
                
                <hr/>
                <div style={{display:'flex', justifyContent:"space-around",alignItems:"center"}}>
                    <div style={isLikePost? {cursor:'pointer'} :{color:"#cc5104",cursor:'pointer'}}>
                        <i onClick={()=>setIsLikePost(!isLikePost)} className="fas fa-child fa-2x"></i>
                    </div>
                    <div style={isLikePost? {color:"#cc5104",cursor:'pointer'}:{cursor:'pointer'}}>
                        <i onClick={()=>setIsLikePost(!isLikePost)} className="fas fa-hand-holding-heart fa-2x"></i>
                    </div>
                </div>
                <hr />
                {
                    !isLikePost
                    ? <PostThumb posts = {profile.posts}/>
                    : <PostThumb posts = {profile.likePosts}/>
                }
            
            </div>
        }
        </>
    )
}

export default ProfileScreen
