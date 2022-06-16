export const checkProfileImage = (file) =>{
    let err = ""
    if(!file) return err = "File does not exist."
    if(file.size> 1024*1024){
        err = "The largest image size is 1mb."
    }
    if(file.type !== 'image/jpeg' && file.type !== 'image/png'){
        err = "Image format is incorrect."
    }
    return err;
}
export const checkPostImage = (file) =>{
    let err = ""
    if(!file) err = "File doesn't exist."
    if(file.size >1024*1024*10){
        err = "The image largest is 10mb."
    }
    if(file.type !== 'image/jpeg' && file.type !=='image/png' && file.type !== 'video/mp4' && file.type !== 'video/avi' && file.type !== 'video/wmv' && file.type !=='video/mov'){
        err = "File format is incorrect."
    }

    return err;
}